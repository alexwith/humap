package com.github.alexwith.humap.entity.spec;

import com.github.alexwith.humap.entity.Entity;
import java.util.HashMap;
import java.util.Map;

public class EntitySpecManagerImpl implements EntitySpecManager {
    private final Map<Class<? extends Entity>, EntitySpec> entitySpecs = new HashMap<>();

    @Override
    public EntitySpec get(Class<? extends Entity> entityClass) {
        return this.entitySpecs.get(entityClass);
    }

    @Override
    public <T extends Entity> EntitySpec register(Class<T> originClass, Class<? extends T> proxiedClass) {
        final EntitySpec spec = new EntitySpecImpl(originClass);
        this.entitySpecs.put(proxiedClass, spec);
        this.entitySpecs.put(originClass, spec);
        return spec;
    }
}
