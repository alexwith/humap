package com.github.alexwith.humap.proxy.entity;

import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.proxy.Proxy;
import java.util.Map;

public interface EntityProxy<T extends Entity> extends Proxy<T> {

    Map<String, EntityField> getFields();

    Map<String, EntityModifyMethod> getModifyMethods();

    EntityField getField(String name);

    EntityModifyMethod getModifyMethod(String name);
}
