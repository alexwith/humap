package com.github.alexwith.humap.repository.query;

import com.github.alexwith.humap.entity.IdEntity;
import com.github.alexwith.humap.repository.Repository;
import net.bytebuddy.implementation.bind.annotation.AllArguments;

public interface QueryInterceptor<K, T extends IdEntity<K>> {

    /**
     * Assign what repository this interceptor
     * should apply its queries to
     *
     * @param repository The repository
     */
    void assignRepository(Repository<K, T> repository);

    /**
     * The method where the repository queries
     * are intercepted
     *
     * @param args The method args for the query
     * @return The result of the repository query
     */
    Object intercept(@AllArguments Object[] args);
}
