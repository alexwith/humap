package com.github.alexwith.humap.proxy;

public interface Morpher {

    /**
     * Morph method parameters
     * This means changing them
     * to other objects e.g. proxying
     * an object before the method is
     * fully executed
     *
     * @param args The morphed arguments
     * @return The returned object
     */
    <T> T morph(Object... args);
}