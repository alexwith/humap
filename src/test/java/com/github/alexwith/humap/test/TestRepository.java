package com.github.alexwith.humap.test;

import com.github.alexwith.humap.repository.Repository;
import java.util.UUID;

public interface TestRepository extends Repository<UUID, TestEntity> {

    TestEntity findByName(String name);
}
