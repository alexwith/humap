package com.github.alexwith.humap.proxy.creator;

import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.entity.spec.EntityField;
import com.github.alexwith.humap.entity.spec.EntitySpec;
import com.github.alexwith.humap.entity.spec.EntitySpecImpl;
import com.github.alexwith.humap.proxy.ProxyConstants;
import com.github.alexwith.humap.proxy.ProxyCreationContext;
import com.github.alexwith.humap.proxy.decorator.ShadowField;
import com.github.alexwith.humap.util.SneakyThrows;

public class EntityProxyCreator<T extends Entity> extends ProxyCreatorImpl<T> implements ProxyCreator<T> {
    private final EntitySpec spec;

    public EntityProxyCreator(Class<T> originClass, Class<? extends T> clazz) {
        super(originClass, clazz);
        this.spec = new EntitySpecImpl(originClass);
    }

    @Override
    public T create(ProxyCreationContext context) {
        final T entity = SneakyThrows.supply(this.constructor::newInstance);
        this.applyGlobals(entity, context);

        ShadowField.set(entity, ProxyConstants.ENTITY_SPEC_FIELD, this.spec);
        ShadowField.set(entity, ProxyConstants.ENTITY_INTERNAL_ID_FIELD, 10); // TODO implement this

        final Entity origin = (Entity) context.getOrigin();
        for (final EntityField field : this.spec.getFields().values()) {
            Object value = field.getValue(origin);
            if (field.isProxyable()) {
                value = this.proxy(value, field.getType(), context.getDirtyTracker());
            }

            field.setValue(entity, value);
        }

        return entity;
    }
}
