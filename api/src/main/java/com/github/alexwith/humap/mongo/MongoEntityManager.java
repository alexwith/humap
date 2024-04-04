package com.github.alexwith.humap.mongo;

import com.github.alexwith.humap.entity.IdEntity;
import java.util.List;
import org.bson.conversions.Bson;

public interface MongoEntityManager<K, T extends IdEntity<K>> {

    T findOne(Bson query);

    List<T> findAll(Bson query);

    void save(T entity);

    void delete(T entity);
}
