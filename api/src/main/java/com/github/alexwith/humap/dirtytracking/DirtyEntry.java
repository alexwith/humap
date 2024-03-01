package com.github.alexwith.humap.dirtytracking;

public interface DirtyEntry {

    DirtyType getType();

    Object[] getPath();
}
