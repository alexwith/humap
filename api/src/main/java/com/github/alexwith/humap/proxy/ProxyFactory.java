package com.github.alexwith.humap.proxy;

import com.github.alexwith.humap.entity.IdEntity;
import com.github.alexwith.humap.proxy.creator.ProxyCreator;

public interface ProxyFactory {

    <T> T proxy(ProxyCreationContext context);

    <K, T extends IdEntity<K>> T proxyRootEntity(T unproxiedEntity);

    <T> ProxyCreator<T> getProxyCreator(Class<T> clazz);

    <T> ProxyCreator<T> getProxyCreator(Class<T> clazz, ProxyCreatorMaker<T> onAbsent);

    interface ProxyCreatorMaker<T> {

        ProxyCreator<T> make(Class<? extends T> proxiedClass);
    }
}