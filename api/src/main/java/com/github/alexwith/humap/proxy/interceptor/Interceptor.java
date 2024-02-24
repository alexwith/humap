package com.github.alexwith.humap.proxy.interceptor;

import com.github.alexwith.humap.proxy.Morpher;
import com.github.alexwith.humap.proxy.Proxy;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import net.bytebuddy.dynamic.DynamicType;

public interface Interceptor<T, R> {

    R intercept(T object, Proxy proxy, Method method, Callable<?> superMethod, Morpher morpher, Object[] args);

    DynamicType.Builder<?> apply(DynamicType.Builder<?> builder);
}
