package com.github.alexwith.humap.mongo;

import com.github.alexwith.humap.entity.IdEntity;
import com.github.alexwith.humap.entity.spec.EntitySpec;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MongoConnectionImpl implements MongoConnection {
    private final Map<Class<?>, MongoEntityManager> entityManagers = new ConcurrentHashMap<>();

    private MongoClient client;
    private MongoDatabase database;

    @Override
    public <K, T extends IdEntity<K>> MongoEntityManager getEntityManager(Class<T> clazz) {
        return this.entityManagers.computeIfAbsent(clazz, ($) -> new MongoEntityManagerImpl<>(clazz));
    }

    @Override
    public MongoClient getClient() {
        return this.client;
    }

    @Override
    public MongoDatabase getDatabase() {
        return this.database;
    }

    @Override
    public void connect(String uri, String database) {
        this.client = MongoClients.create(uri);
        this.database = this.client.getDatabase(database);
    }
}
