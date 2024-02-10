package com.github.alexwith.humap.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnectionImpl implements MongoConnection {
    private MongoClient client;
    private MongoDatabase database;

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
