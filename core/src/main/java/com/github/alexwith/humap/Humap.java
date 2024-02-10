package com.github.alexwith.humap;

import com.github.alexwith.humap.mongo.MongoConnection;
import com.github.alexwith.humap.mongo.MongoConnectionImpl;
import com.github.alexwith.humap.proxy.ProxyFactory;
import com.github.alexwith.humap.proxy.ProxyFactoryImpl;

public class Humap {
    private final ProxyFactory proxyFactory;
    private final MongoConnection connection;

    private static final Humap INSTANCE = new Humap();

    public Humap() {
        this.proxyFactory = new ProxyFactoryImpl();
        this.connection = new MongoConnectionImpl();
    }

    public static Humap get() {
        return INSTANCE;
    }

    public ProxyFactory getProxyFactory() {
        return this.proxyFactory;
    }

    public MongoConnection getConnection() {
        return this.connection;
    }

    public MongoConnection connect(String uri, String database) {
        this.connection.connect(uri, database);

        return this.connection;
    }
}