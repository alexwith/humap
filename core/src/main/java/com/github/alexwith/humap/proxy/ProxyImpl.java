package com.github.alexwith.humap.proxy;

import com.github.alexwith.humap.util.SneakyThrows;
import java.lang.reflect.Constructor;

public abstract class ProxyImpl<T> implements Proxy<T> {
    protected final Class<T> originClass;
    protected final Class<? extends T> clazz;
    protected final Constructor<? extends T> constructor;

    public ProxyImpl(Class<T> originClass, Class<? extends T> clazz) {
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
}
