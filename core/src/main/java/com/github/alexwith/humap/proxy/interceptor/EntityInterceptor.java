package com.github.alexwith.humap.proxy.interceptor;

import com.github.alexwith.humap.annotation.Modifies;
import com.github.alexwith.humap.dirtytracking.DirtyTracker;
import com.github.alexwith.humap.dirtytracking.DirtyType;
import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.entity.spec.EntityField;
import com.github.alexwith.humap.entity.spec.EntityModifyMethod;
import com.github.alexwith.humap.entity.spec.EntitySpec;
import com.github.alexwith.humap.proxy.Proxy;
import com.github.alexwith.humap.proxy.decorator.InterceptorImpl;
import com.github.alexwith.humap.proxy.morphing.Morpher;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

public class EntityInterceptor extends InterceptorImpl<Entity, Object> {

    @Override
    public ElementMatcher<? super MethodDescription> methodMatcher() {
        return ElementMatchers.isMethod().and(ElementMatchers.isSetter().or(ElementMatchers.isAnnotatedWith(Modifies.class)));
    }

    @Override
    public Object intercept(Entity entity, Proxy proxy, Method method, Callable<?> superMethod, Morpher morpher, Object[] args) {
        final EntitySpec spec = entity.getSpec();
        final DirtyTracker dirtyTracker = proxy.getDirtyTracker();

        final EntityModifyMethod entityMethod = spec.getModifyMethod(method.getName());
        if (entityMethod != null) {
            final EntityField field = entityMethod.getField();
            if (field != null) {
                dirtyTracker.add(DirtyType.ADD, new String[]{field.getName()});
            }
        }

        this.tryProxyArgs(args, proxy);

        return morpher.morph(args);
    }
}

