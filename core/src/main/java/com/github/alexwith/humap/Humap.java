package com.github.alexwith.humap;

import com.github.alexwith.humap.entity.spec.EntitySpecManager;
import com.github.alexwith.humap.entity.spec.EntitySpecManagerImpl;
import com.github.alexwith.humap.executor.ExecutorManager;
import com.github.alexwith.humap.executor.ExecutorManagerImpl;
import com.github.alexwith.humap.instance.Instances;
import com.github.alexwith.humap.mongo.MongoConnection;
import com.github.alexwith.humap.mongo.MongoConnectionImpl;
import com.github.alexwith.humap.proxy.ProxyFactory;
import com.github.alexwith.humap.proxy.ProxyFactoryImpl;
import com.github.alexwith.humap.repository.RepositoryManager;
import com.github.alexwith.humap.repository.RepositoryManagerImpl;

public class Humap {
    private final ExecutorManager executorManager;
    private final ProxyFactory proxyFactory;
    private final MongoConnection connection;
    private final EntitySpecManager entitySpecManager;
    private final RepositoryManager repositoryManager;

    private static final Humap INSTANCE = new Humap();

    public Humap() {
        this.executorManager = new ExecutorManagerImpl();
        this.proxyFactory = new ProxyFactoryImpl();
        this.connection = new MongoConnectionImpl();
        this.entitySpecManager = new EntitySpecManagerImpl();
        this.repositoryManager = new RepositoryManagerImpl();

        Instances.register(
            this.executorManager,
            this.proxyFactory,
            this.connection,
            this.entitySpecManager,
            this.repositoryManager
        );
    }

    public static Humap get() {
        return INSTANCE;
    }

    public ExecutorManager getExecutorManager() {
        return this.executorManager;
    }

    public ProxyFactory getProxyFactory() {
        return this.proxyFactory;
    }

    public MongoConnection getConnection() {
        return this.connection;
    }

    public EntitySpecManager getEntitySpecManager() {
        return this.entitySpecManager;
    }

    public RepositoryManager getRepositoryManager() {
        return this.repositoryManager;
    }

    public MongoConnection connect(String uri, String database) {
        this.connection.connect(uri, database);

        return this.connection;
    }
}