package com.github.alexwith.humap.dirtytracking;

public interface DirtyTracker {

    /**
     * If the entity has any dirty entries,
     * this checks normal fields and collections
     *
     * @return Are of the entities fields dirty
     */
    boolean isDirty();

    /**
     * When a field should be marked as dirty
     * If the field is in a sub entity it should
     * be seperated by "." e.g. "field1.field2"
     *
     * @param path The path to the dirty field
     */
    void add(String path);

    boolean isDirty(String path);

    /**
     * If the entire entity is dirty,
     * this would be if the entity a new one
     *
     * @return If the entire entity is dirty
     */
    boolean isAllDirty();

    void setAllDirty(boolean allDirty);

    void takeCollectionSnapshots();
}
