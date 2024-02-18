package com.github.alexwith.humap.proxy.entity;

import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.type.ParamedType;
import java.lang.reflect.Field;

public interface EntityField {

    Field getField();

    String getName();

    ParamedType getType();

    boolean isProxyable();

    <T> T getValue(Entity entity);

    void setValue(Entity entity, Object value);
}
