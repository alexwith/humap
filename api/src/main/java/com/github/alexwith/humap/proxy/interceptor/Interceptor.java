package com.github.alexwith.humap.proxy.interceptor;

import com.github.alexwith.humap.proxy.Morpher;
import com.github.alexwith.humap.proxy.Proxy;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import net.bytebuddy.dynamic.DynamicType;

public interface Interceptor<T, R> {

    /**
     * This is called when a method is intercepted
     * according to some sort of ElementMatcher
     *
     * @param object The proxied object
     * @param proxy The proxy parent of the object
     * @param method The method called on the object
     * @param superMethod The super method called on the object
     * @param morpher Morpher to morph the method arguments
     * @param args The arguments of the method
     * @return The returned object from the method
     */
    R intercept(T object, Proxy proxy, Method method, Callable<?> superMethod, Morpher morpher, Object[] args);

    /**
     * Modifies the builder by applying the
     * interceptor and returns the modified
     * builder
     *
     * @param builder The builder to append to
     * @return The modified builder
     */
    DynamicType.Builder<?> apply(DynamicType.Builder<?> builder);
}
