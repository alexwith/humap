package com.github.alexwith.humap.test;

import com.github.alexwith.humap.entity.IdEntity;
import java.util.function.Consumer;

public class TestHumap {

    public <K, T extends IdEntity<K>> void applyEntity(T entity, Consumer<T> consumer) {
        entity.save();
        try {
            consumer.accept(entity);
        } finally {
            entity.delete();
        }
    }
}
