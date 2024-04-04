package com.github.alexwith.humap.entity;

import com.github.alexwith.humap.mongo.MongoEntityManager;

public interface IdEntity<K> extends Entity {

    K getId();

    @SuppressWarnings("unchecked")
    default void save() {
        MongoEntityManager.get(this.getClass()).save(this);
    }

    @SuppressWarnings("unchecked")
    default void delete() {
        MongoEntityManager.get(this.getClass()).delete(this);
    }
}
