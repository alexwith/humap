package com.github.alexwith.hunmap.test;

import com.github.alexwith.humap.annotation.Collection;
import com.github.alexwith.humap.annotation.EntityId;
import com.github.alexwith.humap.annotation.Modifies;
import com.github.alexwith.humap.entity.IdEntity;
import java.util.List;
import java.util.UUID;
import java.util.function.UnaryOperator;

@Collection("user")
public class User implements IdEntity<UUID> {

    @EntityId
    private UUID id;

    private String name;

    private int score;

    private List<String> history;

    public User(String name, int score, List<String> history) {
        this.name = name;
        this.score = score;
        this.history = history;
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

    public List<String> getHistory() {
        return this.history;
    }
}
