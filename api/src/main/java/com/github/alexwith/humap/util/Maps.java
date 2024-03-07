package com.github.alexwith.humap.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Maps {

    public static <K, V> HashMap<K, V> hashMap(Object... objects) {
        return Maps.populate(new HashMap<>(), objects);
    }

    public static <K, V> LinkedHashMap<K, V> linkedHashMap(Object... objects) {
        return Maps.populate(new LinkedHashMap<>(), objects);
    }

    @SuppressWarnings("unchecked")
    public static <K, V, T extends Map<K, V>> T populate(T map, Object... objects) {
        for (int i = 0; i < objects.length; i += 2) {
            map.put((K) objects[i], (V) objects[i + 1]);
        }

        return map;
    }
}
