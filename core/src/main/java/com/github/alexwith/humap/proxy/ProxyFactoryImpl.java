package com.github.alexwith.humap.proxy;

import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.entity.IdEntity;
import com.github.alexwith.humap.exception.NonProxyableClassException;
import com.github.alexwith.humap.proxy.creator.EntityProxyCreatorImpl;
import com.github.alexwith.humap.proxy.creator.ProxyCreator;
import com.github.alexwith.humap.proxy.decorator.Decorator;
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
        final Class<T> type = (Class<T>) context.getType();

        final ProxyCreator<T> proxyCreator = this.getProxyCreator(type);
        return proxyCreator.create(context);
    }

    @Override
    public <K, T extends IdEntity<K>> T proxyRootEntity(T unproxiedEntity, boolean isNew) {
        return this.proxy(ProxyCreationContextImpl.of((builder) -> builder
            .origin(unproxiedEntity)
            .type(unproxiedEntity.getClass())
            .path("")
            .isNew(isNew)
        ));
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

        for (final Decorator decorator : ProxyConstants.DECORATORS) {
            builder = (DynamicType.Builder<T>) decorator.apply(builder);
        }

        final Class<? extends T> proxiedClass = builder.make().load(CLASS_LOADER).getLoaded();
        final ProxyCreator<T> proxyCreator = onAbsent.make(proxiedClass);
        this.proxyCreators.put(clazz, proxyCreator);

        return proxyCreator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ProxyCreator<T> getProxyCreator(Class<T> clazz) {
        if (Entity.class.isAssignableFrom(clazz)) {
            return (ProxyCreator<T>) this.getEntityProxy((Class<? extends Entity>) clazz);
        }

        throw new NonProxyableClassException(clazz);
    }

    private <T extends Entity> ProxyCreator<T> getEntityProxy(Class<T> clazz) {
        return this.getProxyCreator(clazz, (proxiedClass) -> new EntityProxyCreatorImpl<>(clazz, proxiedClass));
    }
}
