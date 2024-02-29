package com.github.alexwith.humap.dirtytracking;

public interface DirtyTracker {

    void add(DirtyType type, Object[] path);

    enum DirtyType {

        ADD,
        REMOVE
    }
}
