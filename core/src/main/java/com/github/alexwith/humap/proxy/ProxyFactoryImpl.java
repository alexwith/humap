package com.github.alexwith.humap.proxy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;

public class ProxyFactoryImpl implements ProxyFactory {

    private static final ClassLoader CLASS_LOADER = ProxyFactory.class.getClassLoader();

    @Override
    @SuppressWarnings("unchecked")
    public <T> Proxy<T> createProxy(Class<T> clazz) {
        final DynamicType.Builder<T> builder = new ByteBuddy().subclass(clazz);

        final Class<? extends T> proxiedClass = builder.make().load(CLASS_LOADER).getLoaded();
        final Proxy<T> proxy = new ProxyImpl<>(clazz, proxiedClass);

        return proxy;
    }
}
