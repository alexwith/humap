package com.github.alexwith.humap.proxy;

import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.entity.spec.EntitySpec;
import com.github.alexwith.humap.proxy.decorator.Decorator;
import com.github.alexwith.humap.proxy.decorator.ShadowFieldImpl;
import com.github.alexwith.humap.proxy.interceptor.ToStringInterceptor;
import java.util.Map;
import java.util.Set;

public class ProxyConstants {

    public static final Map<Class<?>, Set<Decorator>> DECORATORS = Map.of(
        Entity.class, Set.of(
            new ToStringInterceptor(),
            ShadowFieldImpl.of("spec", EntitySpec.class)
                .withGetter()
        )
    );
}
