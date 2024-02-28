package com.github.alexwith.humap.proxy;

import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.entity.spec.EntitySpec;
import com.github.alexwith.humap.proxy.decorator.Decorator;
import com.github.alexwith.humap.proxy.decorator.ShadowFieldImpl;
import com.github.alexwith.humap.proxy.interceptor.ToStringInterceptor;
import java.util.Map;
import java.util.Set;

public class ProxyConstants {

    public static final String PREFIX = "humap$";
    public static final String ENTITY_SPEC_FIELD = PREFIX + "spec";

    public static final Map<Class<?>, Set<Decorator>> DECORATORS = Map.of(
        Entity.class, Set.of(
            new ToStringInterceptor(),
            ShadowFieldImpl.of(ENTITY_SPEC_FIELD, EntitySpec.class)
                .withGetter()
        )
    );
}
