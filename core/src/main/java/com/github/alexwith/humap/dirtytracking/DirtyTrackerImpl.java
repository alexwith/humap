package com.github.alexwith.humap.dirtytracking;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DirtyTrackerImpl implements DirtyTracker {
    private final Set<String> entries = ConcurrentHashMap.newKeySet();

    private boolean allDirty;

    public DirtyTrackerImpl(boolean allDirty) {
        this.allDirty = allDirty;
    }

    @Override
    public Set<String> getDirty() {
        return this.entries;
    }

    @Override
    public void add(String path) {
        if (this.allDirty) {
            return;
        }

        this.entries.add(path);
    }

    @Override
    public boolean isAllDirty() {
        return this.allDirty;
    }
}
