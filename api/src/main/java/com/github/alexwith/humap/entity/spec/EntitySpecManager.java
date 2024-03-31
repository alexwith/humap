package com.github.alexwith.humap.entity.spec;

import com.github.alexwith.humap.entity.Entity;

public interface EntitySpecManager {

    EntitySpec get(Class<? extends Entity> entityClass);

    <T extends Entity> EntitySpec register(Class<T> originClass, Class<? extends T> proxiedClass);}
