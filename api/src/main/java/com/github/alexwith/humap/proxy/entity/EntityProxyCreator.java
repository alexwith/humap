package com.github.alexwith.humap.proxy.entity;

import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.proxy.ProxyCreator;
import java.util.Map;

public interface EntityProxyCreator<T extends Entity> extends ProxyCreator<T> {

    Map<String, EntityField> getFields();

    Map<String, EntityModifyMethod> getModifyMethods();

    EntityField getField(String name);

    EntityModifyMethod getModifyMethod(String name);
}
