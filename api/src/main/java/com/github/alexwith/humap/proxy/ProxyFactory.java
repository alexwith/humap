package com.github.alexwith.humap.proxy;

public interface ProxyFactory {

    <T> T proxy(ProxyCreationContext context);

    <T> ProxyCreator<T> getProxyCreator(Class<T> clazz);

    <T> ProxyCreator<T> getProxyCreator(Class<T> clazz, ProxyCreatorMaker<T> onAbsent);

    interface ProxyCreatorMaker<T> {

        ProxyCreator<T> make(Class<? extends T> proxiedClass);
    }
}