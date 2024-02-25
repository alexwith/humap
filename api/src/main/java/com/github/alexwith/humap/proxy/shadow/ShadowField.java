package com.github.alexwith.humap.proxy.shadow;

import net.bytebuddy.dynamic.DynamicType;

/**
 * A shadow field is a field that exists in bytecode, but not the source files
 * These are fields created by bytebuddy
 */
public interface ShadowField {

    /**
     * Adds a getter to the shadow field
     *
     * @return This shadow field
     */
    ShadowField withGetter();

    /**
     * Adds a setter to the shadow field
     *
     * @return This shadow field
     */
    ShadowField withSetter();

    /**
     * Modifies the builder by applying the
     * shadow field and returns the modified
     * builder
     *
     * @param builder The builder to append to
     * @return The modified builder
     */
    <T, U extends DynamicType.Builder<T>> U apply(U builder);
}
