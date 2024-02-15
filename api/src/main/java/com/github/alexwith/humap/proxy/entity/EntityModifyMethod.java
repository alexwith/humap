package com.github.alexwith.humap.proxy.entity;

import java.lang.reflect.Method;

public interface EntityModifyMethod {

    Method getMethod();

    String getName();

    /**
     * The field modified by this method,
     * this will generally be setters or
     * other smaller operations on this field
     *
     * @return The field that is modified
     */
    EntityField getField();
}
