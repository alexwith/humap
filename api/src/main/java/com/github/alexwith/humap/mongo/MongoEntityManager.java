package com.github.alexwith.humap.mongo;

import com.github.alexwith.humap.entity.IdEntity;
import org.bson.conversions.Bson;

public interface MongoEntityManager {

    IdEntity<?> findOne(Bson query);

    void save(IdEntity<?> entity);
}
