package com.github.alexwith.humap.entity;

import com.github.alexwith.humap.instance.Instances;
import com.github.alexwith.humap.mongo.MongoConnection;
import com.github.alexwith.humap.mongo.MongoEntityManager;

public interface IdEntity<K> extends Entity {

    K getId();

    @SuppressWarnings("unchecked")
    default void save() {
        final MongoConnection connection = Instances.get(MongoConnection.class);
        final MongoEntityManager<K, IdEntity<K>> manager = connection.getEntityManager(this.getClass());
        manager.save(this);
    }

    @SuppressWarnings("unchecked")
    default void delete() {
        final MongoConnection connection = Instances.get(MongoConnection.class);
        final MongoEntityManager<K, IdEntity<K>> manager = connection.getEntityManager(this.getClass());
        manager.delete(this);
    }
}
