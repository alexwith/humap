package com.github.alexwith.humap.proxy.decorator;

import com.github.alexwith.humap.proxy.ProxyConstants;
import java.lang.reflect.Type;
import net.bytebuddy.description.modifier.FieldPersistence;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FieldAccessor;

public class ShadowFieldImpl implements ShadowField {
    private final String name;
    private final Type type;
    private final String capitalizedName;

    private boolean getter;
    private boolean setter;

    private ShadowFieldImpl(String name, Type type) {
        this.name = name;
        this.type = type;
        this.capitalizedName = this.sanitizedCapitalizedName();
    }

    public static ShadowField of(String name, Type type) {
        return new ShadowFieldImpl(name, type);
    }

    @Override
    public ShadowField withGetter() {
        this.getter = true;
        return this;
    }

    @Override
    public ShadowField withSetter() {
        this.setter = true;
        return this;
    }

    @Override
    public DynamicType.Builder<?> apply(DynamicType.Builder<?> builder) {
        builder = builder.defineField(this.name, this.type, FieldPersistence.TRANSIENT, Visibility.PUBLIC);

        if (this.getter) {
            final String prefix = this.type == boolean.class ? "is" : "get";
            builder = builder
                .defineMethod(prefix.concat(this.capitalizedName), this.type, Visibility.PUBLIC)
                .intercept(FieldAccessor.ofField(this.name));
        }

        if (this.setter) {
            builder = builder
                .defineMethod("set".concat(this.capitalizedName), void.class, Visibility.PUBLIC)
                .withParameters(this.type)
                .intercept(FieldAccessor.ofField(this.name));
        }

        return builder;
    }

    private String sanitizedCapitalizedName() {
        final String name = this.name.replace(ProxyConstants.PREFIX, "");
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}
