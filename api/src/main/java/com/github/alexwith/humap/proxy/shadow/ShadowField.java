package com.github.alexwith.humap.proxy.shadow;

import net.bytebuddy.dynamic.DynamicType;

/**
 * A shadow field is a field that exists in bytecode, but not the source files
 * These are fields created by bytebuddy
 */
public interface ShadowField {

    ShadowField withGetter();

    ShadowField withSetter();

    <T, U extends DynamicType.Builder<T>> U apply(U builder);
}
