package com.github.alexwith.humap.proxy;

import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.entity.spec.EntitySpec;
import com.github.alexwith.humap.exception.NonProxyableClassException;
import com.github.alexwith.humap.proxy.collection.CollectionProxyCreator;
import com.github.alexwith.humap.proxy.entity.EntityProxyCreator;
import com.github.alexwith.humap.proxy.interceptor.Interceptor;
import com.github.alexwith.humap.proxy.interceptor.ToStringInterceptor;
import com.github.alexwith.humap.proxy.map.MapProxyCreator;
import com.github.alexwith.humap.proxy.shadow.ShadowField;
import com.github.alexwith.humap.proxy.shadow.ShadowFieldImpl;
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
    private static final Map<Class<?>, Set<Interceptor<?, ?>>> INTERCEPTORS = Map.of(
        Entity.class, Set.of(
            new ToStringInterceptor()
        )
    );
    private static final Set<ShadowField> SHADOW_FIELDS = Set.of(
        ShadowFieldImpl.of("spec", EntitySpec.class)
            .withGetter()
    );

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

        DynamicType.Builder<T> builder = new ByteBuddy().subclass(clazz).implement(Proxy.class);
        builder = this.applyShadowFields(builder);
        builder = this.applyInterceptors(clazz, builder);

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
    private <T, U extends DynamicType.Builder<T>> U applyInterceptors(Class<T> clazz, U builder) {
        for (final Map.Entry<Class<?>, Set<Interceptor<?, ?>>> entry : INTERCEPTORS.entrySet()) {
            final Class<?> targetClass = entry.getKey();
            if (!targetClass.isAssignableFrom(clazz)) {
                continue;
            }

            for (final Interceptor<?, ?> interceptor : entry.getValue()) {
                builder = (U) interceptor.apply(builder);
            }
        }

        return builder;
    }

    private <T, U extends DynamicType.Builder<T>> U applyShadowFields(U builder) {
        for (final ShadowField field : SHADOW_FIELDS) {
            builder = field.apply(builder);
        }

        return builder;
    }
}
