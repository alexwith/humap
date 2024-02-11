package com.github.alexwith.humap.proxy;

import java.lang.reflect.Constructor;

public interface Proxy<T> {

    Class<T> getOriginClass();

    Class<? extends T> getClazz();

    Constructor<? extends T> getConstructor();
}
