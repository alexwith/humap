package com.github.alexwith.humap.proxy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;

public class ProxyFactoryImpl implements ProxyFactory {
    private final Map<Class<?>, Proxy<?>> proxies = new ConcurrentHashMap<>();

    private static final ClassLoader CLASS_LOADER = ProxyFactory.class.getClassLoader();

    @Override
    @SuppressWarnings("unchecked")
    public <T> Proxy<T> createProxy(Class<T> clazz) {
        if (this.proxies.containsKey(clazz)) {
            return (Proxy<T>) this.proxies.get(clazz);
        }

        final DynamicType.Builder<T> builder = new ByteBuddy().subclass(clazz);

        final Class<? extends T> proxiedClass = builder.make().load(CLASS_LOADER).getLoaded();
        final Proxy<T> proxy = null; //new EntityProxyImpl<>(clazz, proxiedClass);
        this.proxies.put(clazz, proxy);

        return proxy;
    }
}
