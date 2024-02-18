package com.github.alexwith.humap.proxy.collection;

import com.github.alexwith.humap.proxy.ProxyCreationContext;
import com.github.alexwith.humap.proxy.ProxyCreatorImpl;
import java.util.Collection;

public class CollectionProxyCreator<T extends Collection<?>> extends ProxyCreatorImpl<T> {

    public CollectionProxyCreator(Class<T> originClass, Class<? extends T> clazz) {
        super(originClass, clazz);
    }

    @Override
    public T create(ProxyCreationContext context) {
        return null;
    }
}
