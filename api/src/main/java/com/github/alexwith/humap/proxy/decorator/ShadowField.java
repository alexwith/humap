package com.github.alexwith.humap.proxy.decorator;

import com.github.alexwith.humap.util.SneakyThrows;
import java.lang.reflect.Field;

/**
 * A shadow field is a field that exists in bytecode, but not the source files
 * These are fields created by bytebuddy
 */
public interface ShadowField extends Decorator {

    /**
     * Reflection util for setting a field's value
     * This is located here as this will mostly
     * be done in the context of shadow fields
     *
     * @param target    The target object
     * @param fieldName The target field
     * @param value     The new value for the field
     * @return The field that was modified
     */
    static Field set(Object target, String fieldName, Object value) {
        return SneakyThrows.supply(() -> {
            final Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);

            return field;
        });
    }

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
