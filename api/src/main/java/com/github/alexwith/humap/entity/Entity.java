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
    static <T extends Entity> T proxy(T entity) {
        final ProxyFactory proxyFactory = Instances.get(ProxyFactory.class);
        return proxyFactory.proxyEntity(entity, true);
    }

    /**
     * Calls {@link Entity#proxy(Entity)} with the
     * current entity, these are just different for
     * preference’s sake
     *
     * @return An instance of the proxied entity
     */
    @SuppressWarnings("unchecked")
    default <T extends Entity> T proxy() {
        return Entity.proxy((T) this);
    }

    /**
     * If the entity is new. A new entity is an
     * entity that has been created, but not saved
     * yet
     * <br>
     * This method is implemented in bytecode
     * at runtime
     *
     * @return If the entity is new
     */
    default boolean isNew() {
        return false;
    }
}
