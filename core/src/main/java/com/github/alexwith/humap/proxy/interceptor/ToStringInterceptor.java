package com.github.alexwith.humap.proxy.interceptor;

import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.proxy.Morpher;
import com.github.alexwith.humap.proxy.Proxy;
import com.github.alexwith.humap.proxy.entity.EntityField;
import com.github.alexwith.humap.proxy.entity.EntityProxyCreator;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import net.bytebuddy.matcher.ElementMatchers;

public class ToStringInterceptor extends InterceptorImpl<Entity, String> {

    public ToStringInterceptor() {
        super(ElementMatchers.isToString());
    }

    @Override
    public String intercept(Entity object, Proxy proxy, Method method, Callable<?> superMethod, Morpher morpher, Object[] args) {
        final EntityProxyCreator<Entity> proxyCreator = proxy.getCreator();

        final StringBuilder builder = new StringBuilder("%s={".formatted(proxyCreator.getOriginClass()));
        for (final EntityField field : proxyCreator.getFields().values()) {
            final Object value = field.getValue(object);
            builder.append(field.getName())
                .append(": ")
                .append(value)
                .append(", ");
        }

        return builder.substring(0, builder.length() - 2).concat("}");
    }
}
