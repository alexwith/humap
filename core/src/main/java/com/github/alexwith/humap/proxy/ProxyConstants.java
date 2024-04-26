package com.github.alexwith.humap.proxy;

import com.github.alexwith.humap.dirtytracking.DirtyTracker;
import com.github.alexwith.humap.proxy.decorator.Decorator;
import com.github.alexwith.humap.proxy.decorator.ShadowFieldImpl;
import com.github.alexwith.humap.proxy.interceptor.EntityInterceptor;
import com.github.alexwith.humap.proxy.interceptor.ToStringInterceptor;
import java.util.Set;

public class ProxyConstants {

    public static final String PREFIX = "humap$";
    public static final String PROXY_DIRTY_TRACKER_FIELD = PREFIX + "dirtyTracker";
    public static final String ENTITY_NEW_FIELD = PREFIX + "new";

    public static final Set<Decorator> DECORATORS = Set.of(
        ShadowFieldImpl.of(PROXY_DIRTY_TRACKER_FIELD, DirtyTracker.class).withGetter(),
        ShadowFieldImpl.of(ENTITY_NEW_FIELD, boolean.class).withGetter(),
        new ToStringInterceptor(),
        new EntityInterceptor()
    );
}
