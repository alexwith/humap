package com.github.alexwith.humap.mongo;

import com.github.alexwith.humap.entity.IdEntity;
import com.github.alexwith.humap.entity.spec.EntitySpec;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public interface MongoConnection {

    <K, T extends IdEntity<K>> MongoEntityManager getEntityManager(Class<T> clazz);

    MongoClient getClient();

    MongoDatabase getDatabase();

    void connect(String uri, String database);
}
