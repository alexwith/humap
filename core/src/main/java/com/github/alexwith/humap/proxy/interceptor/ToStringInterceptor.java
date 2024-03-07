package com.github.alexwith.humap.proxy.interceptor;

import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.entity.spec.EntityField;
import com.github.alexwith.humap.entity.spec.EntitySpec;
import com.github.alexwith.humap.proxy.Proxy;
import com.github.alexwith.humap.proxy.decorator.InterceptorImpl;
import com.github.alexwith.humap.proxy.morphing.Morpher;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

public class ToStringInterceptor extends InterceptorImpl<Entity, String> {

    @Override
    public ElementMatcher<? super MethodDescription> methodMatcher() {
        return ElementMatchers.isToString();
    }

    @Override
    public String intercept(Entity object, Proxy proxy, Method method, Callable<?> superMethod, Morpher morpher, Object[] args) {
        final EntitySpec spec = object.getSpec();

        final StringBuilder builder = new StringBuilder("%s={".formatted(spec.getOriginClass()));
        for (final EntityField field : spec.getFields().values()) {
            final Object value = field.getValue(object);
            builder.append(field.getName())
                .append(": ")
                .append(value)
                .append(", ");
        }

        return builder.substring(0, builder.length() - 2).concat("}");
    }
}
