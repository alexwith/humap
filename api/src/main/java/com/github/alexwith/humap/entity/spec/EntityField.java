package com.github.alexwith.humap.entity.spec;

import com.github.alexwith.humap.entity.Entity;
import java.lang.reflect.Field;

public interface EntityField {

    Field getField();

    String getName();

    Class<?> getType();

    /**
     * Returns true if this is field of a type
     * that can/should be proxied, e.g.
     * {@link Entity}
     *
     * @return If this field's value can/should be proxied
     */
    boolean isProxyable();

    /**
     * Gets the value of this field
     * on a specific entity
     *
     * @param entity The entity to query
     * @return The value at this field
     */
    <T> T getValue(Entity entity);

    /**
     * Sets the value of this field
     * on a specified entity
     *
     * @param entity The entity to modify
     * @param value  The value that should be set at this field
     */
    void setValue(Entity entity, Object value);
}
