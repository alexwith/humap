package com.github.alexwith.humap.repository.query;

import org.bson.conversions.Bson;

public interface Query {

    boolean isAsync();

    /**
     * Resolve the query with values
     *
     * @param args The values to fill the query with
     * @return The resolved query as a bson filter
     */
    Bson resolve(Object[] args);
}
