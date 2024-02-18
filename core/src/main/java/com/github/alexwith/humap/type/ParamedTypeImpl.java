package com.github.alexwith.humap.type;

import com.github.alexwith.humap.annotation.FieldType;
import com.github.alexwith.humap.util.SneakyThrows;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ParamedTypeImpl implements ParamedType {
    private final Class<?> root;
    private final Type[] args;

    public ParamedTypeImpl(Type type) {
        if (type instanceof final ParameterizedType parameterizedType) {
            this.root = SneakyThrows.supply(() -> Class.forName(parameterizedType.getTypeName().split("<")[0]));
            this.args = parameterizedType.getActualTypeArguments();
        } else {
            this.root = (Class<?>) type;
            this.args = new Type[0];
        }
    }

    public ParamedTypeImpl(Field field) {
        final FieldType fieldType = field.getAnnotation(FieldType.class);
        this.root = fieldType == null ? field.getType() : fieldType.value();

        if (field.getGenericType() instanceof final ParameterizedType parameterizedType) {
            this.args = parameterizedType.getActualTypeArguments();
        } else {
            this.args = new Type[0];
        }
    }

    @Override
    public Class<?> getRoot() {
        return this.root;
    }

    @Override
    public Type[] getArgs() {
        return this.args;
    }
}
