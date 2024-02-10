package com.github.alexwith.humap.proxy;

public interface ProxyFactory {

    <T> Proxy<T> createProxy(Class<T> clazz);
}