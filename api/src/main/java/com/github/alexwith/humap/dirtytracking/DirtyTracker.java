package com.github.alexwith.humap.dirtytracking;

import java.util.Set;

public interface DirtyTracker {

    Set<DirtyEntry> getDirty();

    void add(DirtyType type, Object[] path);
}
