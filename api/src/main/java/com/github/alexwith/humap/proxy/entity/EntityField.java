package com.github.alexwith.humap.proxy.entity;

import com.github.alexwith.humap.entity.Entity;
import java.lang.reflect.Field;

public interface EntityField {

    Field getField();

    String getName();

    boolean isProxyable();

    <T> T getValue(Entity entity);

    void setValue(Entity entity, Object value);
}
