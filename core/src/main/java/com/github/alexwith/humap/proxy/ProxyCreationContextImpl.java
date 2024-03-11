package com.github.alexwith.humap.proxy;

import com.github.alexwith.humap.dirtytracking.DirtyTracker;
import com.github.alexwith.humap.type.ParamedType;
import java.util.function.UnaryOperator;

public class ProxyCreationContextImpl implements ProxyCreationContext {
    private final Object origin;
    private final ParamedType type;
    private final DirtyTracker dirtyTracker;
    private final Object id;

    private ProxyCreationContextImpl(Object origin, ParamedType type, DirtyTracker dirtyTracker, Object id) {
        this.origin = origin;
        this.type = type;
        this.dirtyTracker = dirtyTracker;
        this.id = id;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ProxyCreationContext of(UnaryOperator<Builder> builder) {
        return builder.apply(new Builder()).build();
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

    public static class Builder {
        private Object origin;
        private ParamedType type;
        private DirtyTracker dirtyTracker;
        private Object id;

        public Builder origin(Object origin) {
            this.origin = origin;
            return this;
        }

        public Builder type(ParamedType type) {
            this.type = type;
            return this;
        }

        public Builder dirtyTracker(DirtyTracker dirtyTracker) {
            this.dirtyTracker = dirtyTracker;
            return this;
        }

        public Builder id(Object id) {
            this.id = id;
            return this;
        }

        public ProxyCreationContext build() {
            return new ProxyCreationContextImpl(this.origin, this.type, this.dirtyTracker, this.id);
        }
    }
}
