package com.github.alexwith.humap.proxy;

import com.github.alexwith.humap.entity.IdEntity;
import com.github.alexwith.humap.exception.NonProxyableClassException;
import com.github.alexwith.humap.proxy.creator.ProxyCreator;

public interface ProxyFactory {

    <T> T proxy(ProxyCreationContext context);

    <K, T extends IdEntity<K>> T proxyRootEntity(T unproxiedEntity, boolean isNew);

    /**
     * Gets or creates the relevant proxy creator
     * for the specified class by using {@link ProxyFactory#getProxyCreator(Class, ProxyCreatorMaker)}
     * <br>
     * If the class can't be proxied {@link NonProxyableClassException}
     * will be thrown
     *
     * @param clazz The class that determines the proxy creator
     * @return An instance of the relevant proxy creator
     */
    <T> ProxyCreator<T> getProxyCreator(Class<T> clazz);

    /**
     * Gets or creates a proxy creator for the
     * specified class by using the provided {@link ProxyCreatorMaker}
     *
     * @param clazz    The class that determines the proxy creator
     * @param onAbsent The maker for the proxy creator
     * @return An instance of the relevant proxy creator
     */
    <T> ProxyCreator<T> getProxyCreator(Class<T> clazz, ProxyCreatorMaker<T> onAbsent);

    @FunctionalInterface
    interface ProxyCreatorMaker<T> {

        /**
         * Makes a proxy creator instance for the specified class
         *
         * @param proxiedClass The type of proxy creator we want
         * @return An instance of the proxy creator
         */
        ProxyCreator<T> make(Class<? extends T> proxiedClass);
    }
}