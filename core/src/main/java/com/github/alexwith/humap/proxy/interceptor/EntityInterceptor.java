package com.github.alexwith.humap.proxy.interceptor;

import com.github.alexwith.humap.annotation.Modifies;
import com.github.alexwith.humap.dirtytracking.DirtyTracker;
import com.github.alexwith.humap.dirtytracking.DirtyType;
import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.entity.spec.EntityField;
import com.github.alexwith.humap.entity.spec.EntityModifyMethod;
import com.github.alexwith.humap.entity.spec.EntitySpec;
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
        final EntitySpec spec = entity.getSpec();
        final EntityModifyMethod entityMethod = spec.getModifyMethod(method.getName());
        if (entityMethod != null) {
            final EntityField field = entityMethod.getField();
            if (field != null) {
                final DirtyTracker dirtyTracker = proxy.getDirtyTracker();
                dirtyTracker.add(DirtyType.ADD, new String[]{field.getName()});
            }
        }

        return SneakyThrows.supply(superMethod::call);
    }
}

