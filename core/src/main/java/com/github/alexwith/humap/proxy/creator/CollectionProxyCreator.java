package com.github.alexwith.humap.proxy.creator;

import com.github.alexwith.humap.proxy.ProxyCreationContext;
import com.github.alexwith.humap.util.SneakyThrows;
import java.util.Collection;

public class CollectionProxyCreator<T extends Collection<?>> extends ProxyCreatorImpl<T> {

    public CollectionProxyCreator(Class<T> originClass, Class<? extends T> clazz) {
        super(originClass, clazz);
    }

    @Override
    public T create(ProxyCreationContext context) {
        final T collection = SneakyThrows.supply(this.constructor::newInstance);
        this.applyGlobals(collection, context);

        return collection;
    }
}
