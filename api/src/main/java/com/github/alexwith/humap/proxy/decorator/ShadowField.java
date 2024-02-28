package com.github.alexwith.humap.proxy.decorator;

/**
 * A shadow field is a field that exists in bytecode, but not the source files
 * These are fields created by bytebuddy
 */
public interface ShadowField extends Decorator {

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
}
