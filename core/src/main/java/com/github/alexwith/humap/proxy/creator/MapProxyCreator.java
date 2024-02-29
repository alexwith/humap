package com.github.alexwith.humap.proxy.creator;

import com.github.alexwith.humap.proxy.ProxyCreationContext;
import com.github.alexwith.humap.util.SneakyThrows;
import java.util.Map;

public class MapProxyCreator<T extends Map<?, ?>> extends ProxyCreatorImpl<T> {

    public MapProxyCreator(Class<T> originClass, Class<? extends T> clazz) {
        super(originClass, clazz);
    }

    @Override
    public T create(ProxyCreationContext context) {
        final T map = SneakyThrows.supply(this.constructor::newInstance);
        this.applyGlobals(map, context);

        return map;
    }
}
