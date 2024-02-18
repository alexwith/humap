package com.github.alexwith.humap.proxy;

import java.lang.reflect.Constructor;

public interface ProxyCreator<T> {

    Class<T> getOriginClass();

    Class<? extends T> getClazz();


    Constructor<? extends T> getConstructor();

    T create(ProxyCreationContext context);
}
