package com.github.alexwith.humap.dirtytracking;

import java.util.Set;

public interface DirtyTracker {

    Set<String> getDirty();

    /**
     * When a field should be marked as dirty
     * If the field is in a sub entity it should
     * be seperated by "." e.g. "field1.field2"
     *
     * @param path The path to the dirty field
     */
    void add(String path);

    /**
     * If the entire entity is dirty,
     * this would be if the entity a new one
     *
     * @return If the entire entity is dirty
     */
    boolean isAllDirty();
}
