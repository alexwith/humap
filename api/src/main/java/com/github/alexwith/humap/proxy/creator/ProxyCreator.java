package com.github.alexwith.humap.proxy.creator;

import com.github.alexwith.humap.proxy.ProxyCreationContext;
import java.lang.reflect.Constructor;

public interface ProxyCreator<T> {

    Class<T> getOriginClass();

    /**
     * Gets the proxied class
     *
     * @return The proxied class
     */
    Class<? extends T> getClazz();

    Constructor<? extends T> getConstructor();

    /**
     * Create a proxied object from a context
     *
     * @param context The context to create an object from
     * @return The proxied object
     */
    T create(ProxyCreationContext context);
}
