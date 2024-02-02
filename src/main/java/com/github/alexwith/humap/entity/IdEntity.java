package com.github.alexwith.humap.entity;

public interface IdEntity<K> extends Entity {

    default void save() {

    }

    K getId();
}
