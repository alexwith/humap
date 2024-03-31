package com.github.alexwith.humap.proxy;

import com.github.alexwith.humap.dirtytracking.DirtyTracker;

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

    DirtyTracker getDirtyTracker();

    boolean isNew();

    String getPath();

    Object getId();
}
