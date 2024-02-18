package com.github.alexwith.humap.proxy;

public interface Proxy {

    static boolean isProxied(Object object) {
        return object instanceof Proxy;
    }

    static <U, T extends ProxyCreator<U>> T getCreator(Object object) {
        return Proxy.isProxied(object) ? ((Proxy) object).getCreator() : null;
    }

    <U, T extends ProxyCreator<U>> T getCreator();
}
