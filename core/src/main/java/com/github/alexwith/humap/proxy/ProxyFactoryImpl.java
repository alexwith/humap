package com.github.alexwith.humap.proxy;

import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.exception.NonProxyableClassException;
import com.github.alexwith.humap.proxy.collection.CollectionProxyCreator;
import com.github.alexwith.humap.proxy.entity.EntityProxyCreatorImpl;
import com.github.alexwith.humap.proxy.map.MapProxyCreator;
import com.github.alexwith.humap.type.ParamedType;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;

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

        DynamicType.Builder<T> builder = new ByteBuddy().subclass(clazz);
        builder = this.testInterceptor(builder);

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
        return this.getProxyCreator(clazz, (proxiedClass) -> new EntityProxyCreatorImpl<>(clazz, proxiedClass));
    }

    private <T extends Collection<?>> ProxyCreator<T> createCollectionProxy(Class<T> clazz) {
        return this.getProxyCreator(clazz, (proxiedClass) -> new CollectionProxyCreator<>(clazz, proxiedClass));
    }

    private <T extends Map<?, ?>> ProxyCreator<T> createMapProxy(Class<T> clazz) {
        return this.getProxyCreator(clazz, (proxiedClass) -> new MapProxyCreator<>(clazz, proxiedClass));
    }

    private <T> DynamicType.Builder<T> testInterceptor(DynamicType.Builder<T> builder) {
        return builder.method(ElementMatchers.any())
            .intercept(MethodDelegation
                .withDefaultConfiguration()
                .to(new TestInterceptor())
            );
    }

    public static class TestInterceptor {

        @RuntimeType
        public void intercept(@This Object subject, @Origin Method method, @SuperCall Callable<?> superMethod, @AllArguments Object[] args) {
            System.out.println("method called: " + method.getName());
        }
    }
}
