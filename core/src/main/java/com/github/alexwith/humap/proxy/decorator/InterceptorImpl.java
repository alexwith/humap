package com.github.alexwith.humap.proxy.decorator;

import com.github.alexwith.humap.proxy.Morpher;
import com.github.alexwith.humap.proxy.Proxy;
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

    public InterceptorImpl(ElementMatcher<? super MethodDescription> methodMatcher) {
        this.methodMatcher = methodMatcher;
    }

    @Override
    public DynamicType.Builder<?> apply(DynamicType.Builder<?> builder) {
        return builder
            .method(this.methodMatcher)
            .intercept(MethodDelegation
                .withDefaultConfiguration()
                .withBinders(Morph.Binder.install(Morpher.class))
                .to(this)
            );
    }

    @RuntimeType
    public R localIntercept(@This T object, @Origin Method method, @SuperCall Callable<?> superMethod, @Morph Morpher morpher, @AllArguments Object[] args) {
        return this.intercept(object, (Proxy) object, method, superMethod, morpher, args);
    }
}
