package com.github.alexwith.humap.proxy.entity;

import com.github.alexwith.humap.annotation.Modifies;
import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.proxy.ProxyCreationContext;
import com.github.alexwith.humap.proxy.ProxyCreatorImpl;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class EntityProxyCreatorImpl<T extends Entity> extends ProxyCreatorImpl<T> implements EntityProxyCreator<T> {
    private final Map<String, EntityField> fields = new HashMap<>();
    private final Map<String, EntityModifyMethod> modifyMethods = new HashMap<>();

    public EntityProxyCreatorImpl(Class<T> originClass, Class<? extends T> clazz) {
        super(originClass, clazz);

        this.populateFields();
        this.populateModifyMethods();
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
    public T create(ProxyCreationContext context) {
        return null;
    }

    private void populateFields() {
        for (final Field field : this.originClass.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            field.setAccessible(true);

            final EntityField entityField = new EntityFieldImpl(field);
            this.fields.put(entityField.getName().toLowerCase(), entityField);
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
}
