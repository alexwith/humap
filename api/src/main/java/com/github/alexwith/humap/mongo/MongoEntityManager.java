package com.github.alexwith.humap.mongo;

import com.github.alexwith.humap.entity.IdEntity;
import org.bson.conversions.Bson;

public interface MongoEntityManager<K, T extends IdEntity<K>> {

    T findOne(Bson query);

    void save(T entity);
}
