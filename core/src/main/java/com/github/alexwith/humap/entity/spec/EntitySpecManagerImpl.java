package com.github.alexwith.humap.entity.spec;

import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.proxy.Proxy;
import java.util.HashMap;
import java.util.Map;

public class EntitySpecManagerImpl implements EntitySpecManager {
    private final Map<Class<? extends Entity>, EntitySpec> entitySpecs = new HashMap<>();

    @Override
    public EntitySpec get(Class<? extends Entity> entityClass) {
        if (Proxy.class.isAssignableFrom(entityClass)) {
            throw new RuntimeException("You can only get the EntitySpec of a proxied entity class");
        }

        return this.entitySpecs.get(entityClass);
    }

    @Override
    public EntitySpec register(Class<? extends Entity> originClass, Class<? extends Entity> proxiedClass) {
        final EntitySpec spec = new EntitySpecImpl(originClass);
        this.entitySpecs.put(proxiedClass, spec);
        this.entitySpecs.put(originClass, spec);
        return spec;
    }
}
