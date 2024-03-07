package com.github.alexwith.humap.proxy;

import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.exception.NonProxyableClassException;
import com.github.alexwith.humap.proxy.creator.CollectionProxyCreator;
import com.github.alexwith.humap.proxy.creator.EntityProxyCreator;
import com.github.alexwith.humap.proxy.creator.MapProxyCreator;
import com.github.alexwith.humap.proxy.creator.ProxyCreator;
import com.github.alexwith.humap.proxy.decorator.Decorator;
import com.github.alexwith.humap.proxy.interceptor.EntityInterceptor;
import com.github.alexwith.humap.type.ParamedType;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
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

        DynamicType.Builder<T> builder = new ByteBuddy()
            .subclass(clazz)
            .name("humap.%s$HumapProxy".formatted(clazz.getName()))
            .implement(Proxy.class);
        builder = this.applyDecorators(clazz, builder);

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
            return (ProxyCreator<T>) this.createCollectionProxy((Class<? extends Collection<?>>) clazz);
        } else if (Map.class.isAssignableFrom(clazz)) {
            return (ProxyCreator<T>) this.createMapProxy((Class<? extends Map<?, ?>>) clazz);
        }

        throw new NonProxyableClassException(clazz);
    }

    private <T extends Entity> ProxyCreator<T> createEntityProxy(Class<T> clazz) {
        return this.getProxyCreator(clazz, (proxiedClass) -> new EntityProxyCreator<>(clazz, proxiedClass));
    }

    private <T extends Collection<?>> ProxyCreator<T> createCollectionProxy(Class<T> clazz) {
        return this.getProxyCreator(clazz, (proxiedClass) -> new CollectionProxyCreator<>(clazz, proxiedClass));
    }

    private <T extends Map<?, ?>> ProxyCreator<T> createMapProxy(Class<T> clazz) {
        return this.getProxyCreator(clazz, (proxiedClass) -> new MapProxyCreator<>(clazz, proxiedClass));
    }

    @SuppressWarnings("unchecked")
    private <T, U extends DynamicType.Builder<T>> U applyDecorators(Class<T> clazz, U builder) {
        for (final Map.Entry<Class<?>, Set<Decorator>> entry : ProxyConstants.DECORATORS.entrySet()) {
            final Class<?> targetClass = entry.getKey();
            if (!targetClass.isAssignableFrom(clazz) || targetClass.equals(EntityInterceptor.class)) {
                continue;
            }

            for (final Decorator decorator : entry.getValue()) {
                builder = (U) decorator.apply(builder);
            }
        }

        return builder;
    }
}
