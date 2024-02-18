package com.github.alexwith.humap.proxy.map;

import com.github.alexwith.humap.proxy.ProxyCreationContext;
import com.github.alexwith.humap.proxy.ProxyCreatorImpl;
import java.util.Map;

public class MapProxyCreator<T extends Map<?, ?>> extends ProxyCreatorImpl<T> {

    public MapProxyCreator(Class<T> originClass, Class<? extends T> clazz) {
        super(originClass, clazz);
    }

    @Override
    public T create(ProxyCreationContext context) {
        return null;
    }
}
