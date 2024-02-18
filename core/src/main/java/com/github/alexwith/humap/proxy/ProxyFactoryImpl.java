package com.github.alexwith.humap.proxy;

import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.exception.NonProxyableClassException;
import com.github.alexwith.humap.proxy.collection.CollectionProxyCreator;
import com.github.alexwith.humap.proxy.entity.EntityProxyCreatorImpl;
import com.github.alexwith.humap.proxy.map.MapProxyCreator;
import com.github.alexwith.humap.type.ParamedType;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;

public class ProxyFactoryImpl implements ProxyFactory {
    private final Map<Class<?>, ProxyCreator<?>> proxyCreators = new ConcurrentHashMap<>();

    private static final ClassLoader CLASS_LOADER = ProxyFactory.class.getClassLoader();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T proxy(ProxyCreationContext context) {
        final ParamedType type = context.getType();
        final Class<T> rootType = (Class<T>) type.getRoot();

        final ProxyCreator<T> proxyCreator = this.getProxyCreator(rootType);
        return proxyCreator.create(context);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ProxyCreator<T> getProxyCreator(Class<T> clazz, ProxyCreatorMaker<T> onAbsent) {
        if (this.proxyCreators.containsKey(clazz)) {
            return (ProxyCreator<T>) this.proxyCreators.get(clazz);
        }

        final DynamicType.Builder<T> builder = new ByteBuddy().subclass(clazz);

        final Class<? extends T> proxiedClass = builder.make().load(CLASS_LOADER).getLoaded();
        final ProxyCreator<T> proxyCreator = onAbsent.make(proxiedClass);
        this.proxyCreators.put(clazz, proxyCreator);

        return proxyCreator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ProxyCreator<T> getProxyCreator(Class<T> clazz) {
        if (Entity.class.isAssignableFrom(clazz)) {
            return (ProxyCreator<T>) this.createEntityProxy((Class<? extends Entity>) clazz);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            return (ProxyCreator<T>) this.createCollection((Class<? extends Collection<?>>) clazz);
        } else if (Map.class.isAssignableFrom(clazz)) {
            return (ProxyCreator<T>) this.createMap((Class<? extends Map<?, ?>>) clazz);
        }

        throw new NonProxyableClassException(clazz);
    }

    private <T extends Entity> ProxyCreator<T> createEntityProxy(Class<T> originClass) {
        return this.getProxyCreator(originClass, (proxiedClass) -> new EntityProxyCreatorImpl<>(originClass, proxiedClass));
    }

    private <T extends Collection<?>> ProxyCreator<T> createCollection(Class<T> originClass) {
        return this.getProxyCreator(originClass, (proxiedClass) -> new CollectionProxyCreator<>(originClass, proxiedClass));
    }

    private <T extends Map<?, ?>> ProxyCreator<T> createMap(Class<T> originClass) {
        return this.getProxyCreator(originClass, (proxiedClass) -> new MapProxyCreator<>(originClass, proxiedClass));
    }
}
