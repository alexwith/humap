package com.github.alexwith.humap.mongo;

import com.github.alexwith.humap.entity.IdEntity;
import com.github.alexwith.humap.instance.Instances;
import java.util.List;
import org.bson.conversions.Bson;

public interface MongoEntityManager<K, T extends IdEntity<K>> {

    static <K, T extends IdEntity<K>> MongoEntityManager<K, T> get(Class<T> clazz) {
        final MongoConnection connection = Instances.get(MongoConnection.class);
        return connection.getEntityManager(clazz);
    }

    T findOne(Bson query);

    List<T> findAll(Bson query);

    void save(T entity);

    void delete(T entity);
}
