package com.github.alexwith.humap.dirtytracking;

import java.util.Arrays;

public class DirtyEntryImpl implements DirtyEntry {
    private final DirtyType type;
    private final Object[] path;

    public DirtyEntryImpl(DirtyType type, Object[] path) {
        this.type = type;
        this.path = path;
    }

    @Override
    public DirtyType getType() {
        return this.type;
    }

    @Override
    public Object[] getPath() {
        return this.path;
    }

    @Override
    public String toString() {
        return "DirtyEntry{" +
               "type=" + this.type +
               ", path=" + Arrays.toString(this.path) +
               '}';
    }
}
