package com.github.alexwith.humap;

import com.github.alexwith.humap.mongo.MongoConnection;

public class Humap {
    private final MongoConnection connection;

    private static final Humap INSTANCE = new Humap();

    public Humap() {
        this.connection = new MongoConnection();
    }

    public static Humap get() {
        return INSTANCE;
    }

    public MongoConnection getConnection() {
        return this.connection;
    }

    public MongoConnection connect(String uri, String database) {
        this.connection.connect(uri, database);

        return this.connection;
    }
}