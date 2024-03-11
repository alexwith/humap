package com.github.alexwith.humap.proxy;

import com.github.alexwith.humap.dirtytracking.DirtyTracker;
import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.entity.IdEntity;
import com.github.alexwith.humap.entity.spec.EntitySpec;
import com.github.alexwith.humap.proxy.decorator.Decorator;
import com.github.alexwith.humap.proxy.decorator.ShadowFieldImpl;
import com.github.alexwith.humap.proxy.interceptor.EntityInterceptor;
import com.github.alexwith.humap.proxy.interceptor.ToStringInterceptor;
import com.github.alexwith.humap.util.Maps;
import java.util.Map;
import java.util.Set;

public class ProxyConstants {

    public static final String PREFIX = "humap$";
    public static final String DIRTY_TRACKER_FIELD = PREFIX + "dirtyTracker";
    public static final String ENTITY_SPEC_FIELD = PREFIX + "spec";
    public static final String ENTITY_INTERNAL_ID_FIELD = PREFIX + "internalId";
    public static final String ID_ENTITY_INTERNAL_ID_COUNTER_FIELD = PREFIX + "internalIdCounter";

    public static final Map<Class<?>, Set<Decorator>> DECORATORS = Maps.hashMap(
        Object.class, Set.of(
            ShadowFieldImpl.of(DIRTY_TRACKER_FIELD, DirtyTracker.class).withGetter(),
            new ToStringInterceptor()
        ),
        Entity.class, Set.of(
            ShadowFieldImpl.of(ENTITY_SPEC_FIELD, EntitySpec.class).withGetter(),
            ShadowFieldImpl.of(ENTITY_INTERNAL_ID_FIELD, long.class).withGetter(),
            new EntityInterceptor()
        ),
        IdEntity.class, Set.of(
            ShadowFieldImpl.of(ID_ENTITY_INTERNAL_ID_COUNTER_FIELD, long.class).withGetter().withSetter()
        )
    );
}
