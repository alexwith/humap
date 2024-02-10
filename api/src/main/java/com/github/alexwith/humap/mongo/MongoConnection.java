package com.github.alexwith.humap.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public interface MongoConnection {

    MongoClient getClient();

    MongoDatabase getDatabase();

    void connect(String uri, String database);
}
