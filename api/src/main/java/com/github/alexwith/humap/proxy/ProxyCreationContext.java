package com.github.alexwith.humap.proxy;

public interface ProxyCreationContext {

    /**
     * Returns the origin object,
     * the object the proxied object
     * is based on
     *
     * @return The origin object
     */
    Object getOrigin();

    Class<?> getType();

    boolean isNew();

    Object getId();
}
