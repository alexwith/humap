package com.github.alexwith.hunmap.test;

import com.github.alexwith.humap.annotation.Collection;
import com.github.alexwith.humap.annotation.EntityId;
import com.github.alexwith.humap.annotation.Modifies;
import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.entity.IdEntity;
import java.util.UUID;
import java.util.function.UnaryOperator;

@Collection("user")
public class User implements IdEntity<UUID> {

    @EntityId
    private UUID id;

    private String name;

    private int score;

    public User(String name, int score) {
        this.name = name;
        this.score = score;
    }

    protected User() {}

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

    public int getScore() {
        return this.score;
    }

    @Modifies("score")
    public void modifyScore(UnaryOperator<Integer> modifier) {
        this.score = modifier.apply(this.score);
    }
}
