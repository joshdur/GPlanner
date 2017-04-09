package com.drk.tools.gplannercore.core;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Bundle {

    private final HashMap<String, Object> bundle = new LinkedHashMap<>();

    public void set(String key, Object object){
        bundle.put(key, object);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> t){
        return (T) bundle.get(key);
    }

}
