package com.github.alexwith.humap.entity;

public interface IdEntity<K> extends Entity {

    K getId();

    default void save() {

    }
}
