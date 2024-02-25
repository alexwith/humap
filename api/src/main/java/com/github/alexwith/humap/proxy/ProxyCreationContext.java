package com.github.alexwith.humap.proxy;

import com.github.alexwith.humap.type.ParamedType;

public interface ProxyCreationContext {

    /**
     * Returns the origin object,
     * the object the proxied object
     * is based on
     *
     * @return The origin object
     */
    Object getOrigin();

    ParamedType getType();

    Object getId();
}
