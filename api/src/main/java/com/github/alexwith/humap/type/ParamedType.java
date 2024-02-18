package com.github.alexwith.humap.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * This is a container for simple and {@link ParameterizedType} types
 * Lets say we have a type {@code X<Y, Z>}, the root would be X, Y and Z
 * would be args
 */
public interface ParamedType {

    /**
     * Get the root type, this will be the type
     * this instance is based on. In the example
     * at the top of this class, this method would
     * return the type X
     *
     * @return The root type
     */
    Class<?> getRoot();

    /**
     * Get the type arguments, this will be all
     * the generic parameters. In the example at
     * the top of this class, this method would return
     * the types Y and Z.
     *
     * @return The root's type arguments
     */
    Type[] getArgs();

    /**
     * Get a specific type argument
     * See {@link ParamedType#getArgs()}
     *
     * @param index The index of the type
     * @return The type argument
     */
    default Type getArg(int index) {
        return this.getArgs()[index];
    }
}