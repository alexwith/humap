package com.github.alexwith.humap.entity.spec;

import com.github.alexwith.humap.annotation.Modifies;
import com.github.alexwith.humap.entity.Entity;
import java.util.Map;
import java.util.Optional;

public interface EntitySpec {

    /**
     * The origin class refers to the
     * non-proxied version of a class
     *
     * @return The non-proxied entity class
     */
    Class<? extends Entity> getOriginClass();

    /**
     * Gets all the fields in the origin class
     * The key in the returned map is the name
     * of the field in lowercase, meanwhile the value
     * contains more information about the field,
     * see {@link EntityField}
     *
     * @return All the fields
     */
    Map<String, EntityField> getFields();

    /**
     * Gets all the methods that are setters
     * or annotated with {@link Modifies}
     * The key in the returned map is the name
     * of the method in lowercase, meanwhile the value
     * contains more information about the method,
     * see {@link EntityModifyMethod}
     *
     * @return All the setters/modifying methods
     */
    Map<String, EntityModifyMethod> getModifyMethods();

    EntityField getField(String name);

    EntityModifyMethod getModifyMethod(String name);

    Optional<String> getCollection();

    EntityField getIdField();
}
