package com.github.alexwith.humap.entity.spec;

import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.proxy.Proxy;
import com.github.alexwith.humap.type.ParamedType;
import com.github.alexwith.humap.type.ParamedTypeImpl;
import com.github.alexwith.humap.util.SneakyThrows;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

public class EntityFieldImpl implements EntityField {
    private final Field field;
    private final String name;
    private final ParamedType type;
    private final boolean isProxyable;
    private final MethodHandle getterHandle;
    private final MethodHandle setterHandle;

    public EntityFieldImpl(Field field) {
        field.setAccessible(true);

        this.field = field;
        this.name = field.getName();
        this.type = new ParamedTypeImpl(field);
        this.isProxyable = Proxy.isProxyable(this.type.getRoot());
        this.getterHandle = this.createMethodHandle(MethodHandles.Lookup::unreflectGetter);
        this.setterHandle = this.createMethodHandle(MethodHandles.Lookup::unreflectSetter);
    }

    @Override
    public Field getField() {
        return this.field;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ParamedType getType() {
        return this.type;
    }

    @Override
    public boolean isProxyable() {
        return this.isProxyable;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue(Entity entity) {
        return (T) SneakyThrows.supply(() -> this.getterHandle.invoke(entity));
    }

    @Override
    public void setValue(Entity entity, Object value) {
        SneakyThrows.run(() -> this.setterHandle.invoke(entity, value));
    }

    private MethodHandle createMethodHandle(MethodHandleCreator creator) {
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        return SneakyThrows.supply(() -> creator.create(lookup, this.field));
    }

    private interface MethodHandleCreator {

        MethodHandle create(MethodHandles.Lookup lookup, Field field) throws Exception;
    }
}
