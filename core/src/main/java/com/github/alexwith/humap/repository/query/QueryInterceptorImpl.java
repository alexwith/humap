package com.github.alexwith.humap.repository.query;

import com.github.alexwith.humap.entity.IdEntity;
import com.github.alexwith.humap.repository.Repository;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import org.bson.conversions.Bson;

public class QueryInterceptorImpl<K, T extends IdEntity<K>> implements QueryInterceptor<K, T> {
    private final Query query;

    private Repository<K, T> repository;

    public QueryInterceptorImpl(Query query) {
        this.query = query;
    }

    @Override
    public void assignRepository(Repository<K, T> repository) {
        this.repository = repository;
    }

    @RuntimeType
    @Override
    public Object intercept(@AllArguments Object[] args) {
        if (this.repository == null || this.query == null) {
            throw new RuntimeException("The interceptor hasn't been assigned a repository.");
        }

        final Bson filter = this.query.resolve(args);
        return this.repository.findOne(filter);
    }
}