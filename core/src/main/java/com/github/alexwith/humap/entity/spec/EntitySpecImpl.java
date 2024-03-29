package com.github.alexwith.humap.entity.spec;

import com.github.alexwith.humap.annotation.Collection;
import com.github.alexwith.humap.annotation.EntityId;
import com.github.alexwith.humap.annotation.Modifies;
import com.github.alexwith.humap.entity.Entity;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class EntitySpecImpl implements EntitySpec {
    private final Class<? extends Entity> originClass;
    private final Map<String, EntityField> fields = new LinkedHashMap<>();
    private final Map<String, EntityModifyMethod> modifyMethods = new HashMap<>();
    private final String collection;

    private EntityField idField;

    public EntitySpecImpl(Class<? extends Entity> originClass) {
        this.originClass = originClass;
        this.collection = this.tryGetCollection();

        this.populateFields();
        this.populateModifyMethods();
    }

    @Override
    public Class<? extends Entity> getOriginClass() {
        return this.originClass;
    }

    @Override
    public Map<String, EntityField> getFields() {
        return this.fields;
    }

    @Override
    public Map<String, EntityModifyMethod> getModifyMethods() {
        return this.modifyMethods;
    }

    @Override
    public EntityField getField(String name) {
        return this.fields.get(name.toLowerCase());
    }

    @Override
    public EntityModifyMethod getModifyMethod(String name) {
        return this.modifyMethods.get(name.toLowerCase());
    }

    @Override
    public Optional<String> getCollection() {
        return Optional.ofNullable(this.collection);
    }

    public EntityField getIdField() {
        return this.idField;
    }

    private void populateFields() {
        for (final Field field : this.originClass.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            field.setAccessible(true);

            final EntityField entityField = new EntityFieldImpl(field);
            this.fields.put(entityField.getName().toLowerCase(), entityField);

            if (field.isAnnotationPresent(EntityId.class)) {
                this.idField = entityField;
            }
        }
    }

    private void populateModifyMethods() {
        for (final Method method : this.originClass.getDeclaredMethods()) {
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }

            final Modifies modifies = method.getAnnotation(Modifies.class);
            final EntityField entityField = this.getField(modifies != null ? modifies.value() : method.getName().replace("set", ""));
            if (entityField == null) {
                continue;
            }

            final EntityModifyMethod proxyMethod = new EntityModifyMethodImpl(method, entityField);
            this.modifyMethods.put(proxyMethod.getName().toLowerCase(), proxyMethod);
        }
    }

    private String tryGetCollection() {
        final Collection collection = this.originClass.getAnnotation(Collection.class);
        return collection.value();
    }
}
