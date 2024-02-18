package com.github.alexwith.humap.proxy;

import com.github.alexwith.humap.type.ParamedType;

public interface ProxyCreationContext {

    Object getOrigin();

    ParamedType getType();

    Object getId();
}
