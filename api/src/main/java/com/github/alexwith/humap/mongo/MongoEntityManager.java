package com.github.alexwith.humap.mongo;

import com.github.alexwith.humap.entity.IdEntity;

public interface MongoEntityManager {

    void save(IdEntity<?> entity);
}
