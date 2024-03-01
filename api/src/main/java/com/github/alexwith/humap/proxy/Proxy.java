package com.github.alexwith.humap.proxy;

import com.github.alexwith.humap.dirtytracking.DirtyTracker;

public interface Proxy {

    static boolean isProxied(Object object) {
        return object instanceof Proxy;
    }

    static Proxy asProxy(Object object) {
        return Proxy.isProxied(object) ? (Proxy) object : null;
    }

    /**
     * Get the root dirty tracker of this proxy
     * <br>
     * This method is implemented in bytecode
     * at runtime
     *
     * @return The dirty tracker of this proxy
     */
    default DirtyTracker getDirtyTracker() {
        return null;
    }
}
