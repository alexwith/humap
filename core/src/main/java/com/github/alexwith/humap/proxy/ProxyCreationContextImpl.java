package com.github.alexwith.humap.proxy;

import com.github.alexwith.humap.type.ParamedType;

public class ProxyCreationContextImpl implements ProxyCreationContext {
    private final Object origin;
    private final ParamedType type;
    private final Object id;

    public ProxyCreationContextImpl(Object origin, ParamedType type) {
        this(origin, type, null);
    }

    public ProxyCreationContextImpl(Object origin, ParamedType type, Object id) {
        this.origin = origin;
        this.type = type;
        this.id = id;
    }

    @Override
    public Object getOrigin() {
        return this.origin;
    }

    @Override
    public ParamedType getType() {
        return this.type;
    }

    @Override
    public Object getId() {
        return this.id;
    }
}
