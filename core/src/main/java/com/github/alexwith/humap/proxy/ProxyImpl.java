package com.github.alexwith.humap.proxy;

import com.github.alexwith.humap.util.SneakyThrows;
import java.lang.reflect.Constructor;

public class ProxyImpl<T> implements Proxy<T> {
    private final Class<T> originClass;
    private final Class<? extends T> clazz;
    private final Constructor<? extends T> constructor;

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
