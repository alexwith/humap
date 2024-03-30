package com.github.alexwith.humap.entity.spec;

import com.github.alexwith.humap.entity.Entity;

public interface EntitySpecManager {

    EntitySpec get(Class<? extends Entity> entityClass);

    EntitySpec register(Class<? extends Entity> originClass, Class<? extends Entity> proxiedClass);
}
