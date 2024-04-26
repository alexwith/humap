package com.github.alexwith.humap.dirtytracking;

import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.entity.spec.EntityField;
import com.github.alexwith.humap.entity.spec.EntitySpec;
import com.github.alexwith.humap.proxy.Proxy;
import com.github.alexwith.humap.util.SneakyThrows;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DirtyTrackerImpl implements DirtyTracker {
    private final Entity entity;
    private final EntitySpec entitySpec;
    private final Set<String> entries = ConcurrentHashMap.newKeySet();
    private final Map<String, Object> collectionSnapshots = new ConcurrentHashMap<>();

    private boolean allDirty;

    public DirtyTrackerImpl(Entity entity, boolean allDirty) {
        this.entity = entity;
        this.entitySpec = EntitySpec.from(entity);
        this.allDirty = allDirty;
    }

    @Override
    public boolean isDirty() {
        for (final EntityField field : this.entitySpec.getFields().values()) {
            final String name = field.getName();
            if (this.isDirty(name)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void add(String path) {
        if (this.allDirty) {
            return;
        }

        this.entries.add(path);
    }

    @Override
    public boolean isDirty(String path) {
        if (this.allDirty || this.entries.contains(path) || this.isCollectionDirty(path)) {
            return true;
        }

        final EntityField field = this.entitySpec.getField(path);
        final Object value = field.getValue(this.entity);
        if (value instanceof final Proxy valueProxy) {
            return valueProxy.getDirtyTracker().isDirty();
        }

        return false;
    }

    @Override
    public boolean isAllDirty() {
        return this.allDirty;
    }

    @Override
    public void setAllDirty(boolean allDirty) {
        this.allDirty = allDirty;
    }

    // A map is also defined as a collection in this context
    @Override
    public void takeCollectionSnapshots() {
        for (final EntityField field : this.entitySpec.getFields().values()) {
            final Object origin = field.getValue(this.entity);
            if (origin == null) {
                continue;
            }

            final Class<?> type = origin.getClass();
            final boolean isCollection = Collection.class.isAssignableFrom(type);
            final boolean isMap = Map.class.isAssignableFrom(type);
            if (!isCollection && !isMap) {
                continue;
            }

            final Object snapshot = SneakyThrows.supply(() -> {
                final Constructor<?> constructor = type.getConstructor(isCollection ? Collection.class : Map.class);
                return constructor.newInstance(origin);
            });

            final String path = field.getName();
            this.collectionSnapshots.put(path, snapshot);
        }
    }

    private boolean isCollectionDirty(String path) {
        final EntityField field = this.entitySpec.getField(path);
        if (!Collection.class.isAssignableFrom(field.getType()) && !Map.class.isAssignableFrom(field.getType())) {
            return false;
        }

        final Object value = field.getValue(this.entity);

        // If any of the entries in the map or collection has a dirty tracker
        // we need to check if it has any dirty properties, if they do our entire
        // collection is dirty
        final Iterable<?> iterable = value instanceof final Map<?, ?> map ? map.values() : (Collection<?>) value;
        for (final Object entry : iterable) {
            if (!(entry instanceof final Proxy entryProxy)) {
                continue;
            }

            final DirtyTracker entryDirtyTracker = entryProxy.getDirtyTracker();
            if (entryDirtyTracker.isAllDirty() || entryDirtyTracker.isDirty()) {
                return true;
            }
        }

        return !this.collectionSnapshots.containsKey(path) || !this.collectionSnapshots.get(path).equals(value);
    }
}
