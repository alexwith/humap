package com.github.alexwith.humap.entity.spec;

import com.github.alexwith.humap.entity.Entity;
import java.util.Map;

public interface EntitySpec {

    Class<? extends Entity> getOriginClass();

    Map<String, EntityField> getFields();

    Map<String, EntityModifyMethod> getModifyMethods();

    EntityField getField(String name);

    EntityModifyMethod getModifyMethod(String name);
}
