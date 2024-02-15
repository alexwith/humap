package com.github.alexwith.humap.entity;

import com.github.alexwith.humap.instance.Instances;
import com.github.alexwith.humap.proxy.ProxyFactory;

public interface Entity {

    /**
     * Proxies the passed entity. This
     * allows Humap to dirty track the
     * entity and is required for the
     * entity to function as expected
     * <br>
     * Note: The returned instance is a completely
     * new instance and different instance to the one
     * passed in
     *
     * @param entity The entity to proxy
     * @return An instance of the proxied entity
     */
    static <K, T extends IdEntity<K>> T create(T entity) {
        final ProxyFactory proxyFactory = Instances.get(ProxyFactory.class);
        return null;
    }
}
