package com.github.alexwith.humap.proxy.decorator;

import com.github.alexwith.humap.proxy.Morpher;
import com.github.alexwith.humap.proxy.Proxy;
import com.github.alexwith.humap.proxy.decorator.Decorator;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public interface Interceptor<T, R> extends Decorator {

    /**
     * This is called when a method is intercepted
     * according to some sort of ElementMatcher
     *
     * @param object      The proxied object
     * @param proxy       The proxy parent of the object
     * @param method      The method called on the object
     * @param superMethod The super method called on the object
     * @param morpher     Morpher to morph the method arguments
     * @param args        The arguments of the method
     * @return The returned object from the method
     */
    R intercept(T object, Proxy proxy, Method method, Callable<?> superMethod, Morpher morpher, Object[] args);
}
