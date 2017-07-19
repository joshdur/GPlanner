package com.drk.tools.gplannercore.core.main;

import com.drk.tools.gplannercore.core.Context;

public abstract class BaseOperators {

    protected final Context context;

    public BaseOperators(Context context) {
        this.context = context;
    }

    protected <T> T inject(Class<T> t) {
        return context.inject(t);
    }


}
