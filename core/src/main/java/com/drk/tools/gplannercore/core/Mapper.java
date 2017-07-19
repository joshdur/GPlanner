package com.drk.tools.gplannercore.core;

import java.util.HashMap;
import java.util.LinkedHashMap;

class Mapper {

    private final HashMap<Class, Object> bundle = new LinkedHashMap<>();

    public void set(Class key, Object object) {
        bundle.put(key, object);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> key) {
        return (T) bundle.get(key);
    }

}
