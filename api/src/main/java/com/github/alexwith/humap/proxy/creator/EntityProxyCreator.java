package com.github.alexwith.humap.proxy.creator;

import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.entity.spec.EntitySpec;

public interface EntityProxyCreator<T extends Entity> extends ProxyCreator<T> {

    EntitySpec getSpec();
}
