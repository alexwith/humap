package com.github.alexwith.humap.proxy.decorator;

import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.entity.spec.EntityField;
import com.github.alexwith.humap.instance.Instances;
import com.github.alexwith.humap.proxy.Proxy;
import com.github.alexwith.humap.proxy.ProxyCreationContext;
import com.github.alexwith.humap.proxy.ProxyCreationContextImpl;
import com.github.alexwith.humap.proxy.ProxyFactoryImpl;
import com.github.alexwith.humap.proxy.morphing.Morpher;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Morph;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatcher;

public abstract class InterceptorImpl<T, R> implements Interceptor<T, R> {
    private final ElementMatcher<? super MethodDescription> methodMatcher;
    private final InterceptionDelegate<T, R> delegate;

    public InterceptorImpl() {
        this.methodMatcher = this.methodMatcher();
        this.delegate = new InterceptionDelegate<>(this);
    }

    @Override
    public DynamicType.Builder<?> apply(DynamicType.Builder<?> builder) {
        return builder
            .method(this.methodMatcher)
            .intercept(MethodDelegation
                .withDefaultConfiguration()
                .withBinders(Morph.Binder.install(Morpher.class))
                .to(this.delegate))
            ;
    }

    protected void tryProxyArgs(Object[] args, Proxy parentProxy, EntityField field) {
        for (int i = 0; i < args.length; i++) {
            args[i] = this.tryProxy(args[i], parentProxy, field);
        }
    }

    protected Object tryProxy(Object object, Proxy parentProxy, EntityField field) {
        if (Proxy.isProxied(object) || !Proxy.isProxyable(object.getClass())) {
            return object;
        }

        final ProxyCreationContext context = ProxyCreationContextImpl.of((builder) -> builder
            .origin(object)
            .type(object.getClass())
            .isNew(parentProxy instanceof final Entity entity && entity.isNew())
        );
        return Instances.get(ProxyFactoryImpl.class).proxy(context);
    }

    // This is needed to avoid an issue where bytebuddy can't find the delegation method
    public static class InterceptionDelegate<T, R> {
        private final Interceptor<T, R> interceptor;

        public InterceptionDelegate(Interceptor<T, R> interceptor) {
            this.interceptor = interceptor;
        }

        @RuntimeType
        public R onMethod(@This T object,
                          @Origin Method method,
                          @SuperCall Callable<?> superMethod,
                          @Morph Morpher morpher,
                          @AllArguments Object[] args) {
            return this.interceptor.intercept(object, (Proxy) object, method, superMethod, morpher, args);
        }
    }
}