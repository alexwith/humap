package com.github.alexwith.humap.proxy.interceptor;

import com.github.alexwith.humap.annotation.Modifies;
import com.github.alexwith.humap.dirtytracking.DirtyTracker;
import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.proxy.Morpher;
import com.github.alexwith.humap.proxy.Proxy;
import com.github.alexwith.humap.proxy.decorator.InterceptorImpl;
import com.github.alexwith.humap.util.SneakyThrows;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import net.bytebuddy.matcher.ElementMatchers;

public class EntityInterceptor extends InterceptorImpl<Entity, Object> {

    public EntityInterceptor() {
        super(ElementMatchers.isMethod().and(ElementMatchers.isSetter().or(ElementMatchers.isAnnotatedWith(Modifies.class))));
    }

    @Override
    public Object intercept(Entity entity, Proxy proxy, Method method, Callable<?> superMethod, Morpher morpher, Object[] args) {
        final DirtyTracker dirtyTracker = proxy.getDirtyTracker();

        return SneakyThrows.supply(superMethod::call);
    }
}

