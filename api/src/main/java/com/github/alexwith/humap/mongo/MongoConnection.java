package com.github.alexwith.humap.mongo;

import com.github.alexwith.humap.entity.IdEntity;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public interface MongoConnection {

    <K, T extends IdEntity<K>> MongoEntityManager getEntityManager(T entity);

    MongoClient getClient();

    MongoDatabase getDatabase();

    void connect(String uri, String database);
}
