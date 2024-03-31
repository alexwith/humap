package com.github.alexwith.humap.util;

import java.util.Map;

public class PrimitiveTypes {

    private static final Map<Class<?>, Class<?>> BOXED_TYPES = Maps.hashMap(
        int.class, Integer.class,
        long.class, Long.class,
        boolean.class, Boolean.class,
        char.class, Character.class,
        byte.class, Byte.class,
        short.class, Short.class,
        float.class, Float.class,
        double.class, Double.class
    );

    public static Class<?> toBoxedType(Class<?> clazz) {
        return BOXED_TYPES.getOrDefault(clazz, clazz);
    }
}
