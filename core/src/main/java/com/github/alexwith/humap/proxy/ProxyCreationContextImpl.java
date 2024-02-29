package com.github.alexwith.humap.proxy;

import com.github.alexwith.humap.dirtytracking.DirtyTracker;
import com.github.alexwith.humap.type.ParamedType;

public class ProxyCreationContextImpl implements ProxyCreationContext {
    private final Object origin;
    private final ParamedType type;
    private final DirtyTracker dirtyTracker;
    private final Object id;

    public ProxyCreationContextImpl(Object origin, ParamedType type, DirtyTracker dirtyTracker) {
        this(origin, type, dirtyTracker, null);
    }

    public ProxyCreationContextImpl(Object origin, ParamedType type, DirtyTracker dirtyTracker, Object id) {
        this.origin = origin;
        this.type = type;
        this.dirtyTracker = dirtyTracker;
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
    public DirtyTracker getDirtyTracker() {
        return this.dirtyTracker;
    }

    @Override
    public Object getId() {
        return this.id;
    }
}
