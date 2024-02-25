package com.github.alexwith.humap.entity.spec;

import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.type.ParamedType;
import java.lang.reflect.Field;
import java.util.Collection;

public interface EntityField {

    Field getField();

    String getName();

    ParamedType getType();

    /**
     * Returns true if this is field of a type
     * that can/should be proxied, e.g.
     * {@link Entity} or {@link Collection}
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
     * @param value The value that should be set at this field
     */
    void setValue(Entity entity, Object value);
}
