package com.github.alexwith.humap.proxy.entity;

import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.entity.spec.EntitySpec;
import com.github.alexwith.humap.entity.spec.EntitySpecImpl;
import com.github.alexwith.humap.proxy.ProxyCreationContext;
import com.github.alexwith.humap.proxy.ProxyCreator;
import com.github.alexwith.humap.proxy.ProxyCreatorImpl;
import com.github.alexwith.humap.util.SneakyThrows;
import java.lang.reflect.Field;

public class EntityProxyCreator<T extends Entity> extends ProxyCreatorImpl<T> implements ProxyCreator<T> {
    private final EntitySpec spec;

    public EntityProxyCreator(Class<T> originClass, Class<? extends T> clazz) {
        super(originClass, clazz);
        this.spec = new EntitySpecImpl(originClass);
    }

    @Override
    public T create(ProxyCreationContext context) {
        final T entity = SneakyThrows.supply(this.constructor::newInstance);

        SneakyThrows.run(() -> { // TODO: This is temp, find a better way
            final Field field = entity.getClass().getDeclaredField("spec");
            field.set(entity, this.spec);
        });

        return entity;
    }
}
