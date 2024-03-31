package com.github.alexwith.humap.proxy.creator;

import com.github.alexwith.humap.dirtytracking.DirtyTracker;
import com.github.alexwith.humap.instance.Instances;
import com.github.alexwith.humap.proxy.ProxyCreationContext;
import com.github.alexwith.humap.proxy.ProxyCreationContextImpl;
import com.github.alexwith.humap.proxy.ProxyFactoryImpl;
import com.github.alexwith.humap.util.SneakyThrows;
import java.lang.reflect.Constructor;

public abstract class ProxyCreatorImpl<T> implements ProxyCreator<T> {
    protected final Class<T> originClass;
    protected final Class<? extends T> clazz;
    protected final Constructor<? extends T> constructor;

    public ProxyCreatorImpl(Class<T> originClass, Class<? extends T> proxiedClass) {
        this.originClass = originClass;
        this.clazz = proxiedClass;
        this.constructor = SneakyThrows.supply(proxiedClass::getDeclaredConstructor);
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

    protected Object proxy(Object object, Class<?> type, DirtyTracker dirtyTracker, String path) {
        final ProxyCreationContext context = ProxyCreationContextImpl.of((builder) -> builder
            .origin(object)
            .type(type)
            .dirtyTracker(dirtyTracker)
            .path(path)
        );
        return Instances.get(ProxyFactoryImpl.class).proxy(context);
    }
}
