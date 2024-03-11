package com.github.alexwith.humap.proxy.creator;

import com.github.alexwith.humap.dirtytracking.DirtyTracker;
import com.github.alexwith.humap.dirtytracking.DirtyTrackerImpl;
import com.github.alexwith.humap.instance.Instances;
import com.github.alexwith.humap.proxy.ProxyConstants;
import com.github.alexwith.humap.proxy.ProxyCreationContext;
import com.github.alexwith.humap.proxy.ProxyCreationContextImpl;
import com.github.alexwith.humap.proxy.ProxyFactoryImpl;
import com.github.alexwith.humap.proxy.decorator.ShadowField;
import com.github.alexwith.humap.type.ParamedType;
import com.github.alexwith.humap.util.SneakyThrows;
import java.lang.reflect.Constructor;
import java.util.Optional;

public abstract class ProxyCreatorImpl<T> implements ProxyCreator<T> {
    protected final Class<T> originClass;
    protected final Class<? extends T> clazz;
    protected final Constructor<? extends T> constructor;

    public ProxyCreatorImpl(Class<T> originClass, Class<? extends T> clazz) {
        this.originClass = originClass;
        this.clazz = clazz;
        this.constructor = SneakyThrows.supply(clazz::getDeclaredConstructor);
    }

    @Override
    public Class<T> getOriginClass() {
        return this.originClass;
    }

    @Override
    public Class<? extends T> getClazz() {
        return this.clazz;
    }

    @Override
    public Constructor<? extends T> getConstructor() {
        return this.constructor;
    }

    protected void applyGlobals(T target, ProxyCreationContext context) {
        ShadowField.set(
            target,
            ProxyConstants.DIRTY_TRACKER_FIELD,
            Optional.ofNullable(context.getDirtyTracker()).orElse(new DirtyTrackerImpl())
        );
    }

    protected Object proxy(Object object, ParamedType type, DirtyTracker dirtyTracker) {
        final ProxyCreationContext context = ProxyCreationContextImpl.of((builder) -> builder
            .origin(object)
            .type(type)
            .dirtyTracker(dirtyTracker)
        );
        return Instances.get(ProxyFactoryImpl.class).proxy(context);
    }
}
