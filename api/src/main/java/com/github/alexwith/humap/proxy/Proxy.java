package com.github.alexwith.humap.proxy;

public interface Proxy {

    static boolean isProxied(Object object) {
        return object instanceof Proxy;
    }
}
