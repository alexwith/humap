package com.github.alexwith.humap.proxy.decorator;

import com.github.alexwith.humap.proxy.Proxy;
import com.github.alexwith.humap.proxy.morphing.Morpher;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

public interface Interceptor<T, R> extends Decorator {

    ElementMatcher<? super MethodDescription> methodMatcher();

    /**
     * This is called when a method is intercepted
     * according to specified {@link Interceptor#methodMatcher()}
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
