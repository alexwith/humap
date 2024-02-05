package com.github.alexwith.humap.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {
    private MongoClient client;
    private MongoDatabase database;

    public void connect(String uri, String database) {
        this.client = MongoClients.create(uri);
        this.database = this.client.getDatabase(database);
    }

    public MongoClient getClient() {
        return this.client;
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }
}
