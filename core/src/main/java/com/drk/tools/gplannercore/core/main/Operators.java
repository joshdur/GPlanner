package com.drk.tools.gplannercore.core.main;

import com.drk.tools.gplannercore.core.Bundle;

public abstract class Operators {

    private Bundle bundle;

    public final void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    @SuppressWarnings("unchecked")
    protected <T> T obtain(String key, Class<T> t) {
        return bundle.get(key, t);
    }
}
