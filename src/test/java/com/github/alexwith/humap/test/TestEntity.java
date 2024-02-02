package com.github.alexwith.humap.test;

import com.github.alexwith.humap.annotation.Collection;
import com.github.alexwith.humap.annotation.EntityId;
import com.github.alexwith.humap.entity.IdEntity;
import java.util.UUID;

@Collection("test-entity")
public class TestEntity implements IdEntity<UUID> {

    @EntityId
    private UUID id;

    private String name;

    public TestEntity(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    protected TestEntity() {}

    @Override
    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
