package com.github.alexwith.humap.entity;

public interface IdEntity<K> extends Entity {

    K getId();

    default long getInternalIdCounter() {
        return -1;
    }

    default void setInternalIdCounter(long count) {}

    default void save() {

    }
}
