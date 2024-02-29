package com.github.alexwith.humap.entity.spec;

import com.github.alexwith.humap.entity.spec.EntityField;
import com.github.alexwith.humap.entity.spec.EntityModifyMethod;
import java.lang.reflect.Method;

public class EntityModifyMethodImpl implements EntityModifyMethod {
    private final Method method;
    private final String name;
    private final EntityField field;

    public EntityModifyMethodImpl(Method method, EntityField field) {
        this.method = method;
        this.name = method.getName();
        this.field = field;
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public EntityField getField() {
        return this.field;
    }
}
