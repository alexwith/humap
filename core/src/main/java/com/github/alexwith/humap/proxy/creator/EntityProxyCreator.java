package com.github.alexwith.humap.proxy.creator;

import com.github.alexwith.humap.dirtytracking.DirtyTracker;
import com.github.alexwith.humap.dirtytracking.DirtyTrackerImpl;
import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.entity.spec.EntityField;
import com.github.alexwith.humap.entity.spec.EntitySpec;
import com.github.alexwith.humap.entity.spec.EntitySpecManager;
import com.github.alexwith.humap.instance.Instances;
import com.github.alexwith.humap.proxy.ProxyConstants;
import com.github.alexwith.humap.proxy.ProxyCreationContext;
import com.github.alexwith.humap.proxy.decorator.ShadowField;
import com.github.alexwith.humap.util.SneakyThrows;
import java.util.Optional;

public class EntityProxyCreator<T extends Entity> extends ProxyCreatorImpl<T> {
    private final EntitySpec spec;

    public EntityProxyCreator(Class<T> originClass, Class<? extends T> proxiedClass) {
        super(originClass, proxiedClass);
        this.spec = Instances.get(EntitySpecManager.class).register(originClass, proxiedClass);
    }

    @Override
    public T create(ProxyCreationContext context) {
        final T entity = SneakyThrows.supply(this.constructor::newInstance);

        final DirtyTracker dirtyTracker = Optional.ofNullable(context.getDirtyTracker()).orElse(new DirtyTrackerImpl(context.isNew()));
        ShadowField.set(entity, ProxyConstants.PROXY_DIRTY_TRACKER_FIELD, dirtyTracker);
        ShadowField.set(entity, ProxyConstants.PROXY_PATH_FIELD, context.getPath());

        final Entity origin = (Entity) context.getOrigin();
        if (origin == null) {
            return entity;
        }

        for (final EntityField field : this.spec.getFields().values()) {
            Object value = field.getValue(origin);
            if (value == null) {
                continue;
            }

            if (field.isProxyable()) {
                final String path = context.getPath().isEmpty() ? field.getName() : context.getPath().concat(field.getName());
                value = this.proxy(value, field.getType(), context.getDirtyTracker(), path);
            }

            field.setValue(entity, value);
        }

        return entity;
    }
}
