package com.github.alexwith.humap.proxy;

import com.github.alexwith.humap.dirtytracking.DirtyTracker;
import com.github.alexwith.humap.entity.Entity;
import java.util.Set;

public interface Proxy {

    Set<Class<?>> PROXYABLE_TYPES = Set.of(
        Entity.class
    );

    static Proxy asProxy(Object object) {
        return Proxy.isProxied(object) ? (Proxy) object : null;
    }

    static boolean isProxied(Object object) {
        return object instanceof Proxy;
    }

    static boolean isProxyable(Class<?> type) {
        for (final Class<?> proxyableType : PROXYABLE_TYPES) {
            if (proxyableType.isAssignableFrom(type)) {
                return true;
            }
        }

        return false;
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
