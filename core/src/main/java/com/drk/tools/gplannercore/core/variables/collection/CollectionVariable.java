package com.drk.tools.gplannercore.core.variables.collection;

import com.drk.tools.gplannercore.core.variables.Variable;

public abstract class CollectionVariable<T> extends Variable<T> {

    public CollectionVariable(T value) {
        super(value);
    }

    protected abstract int toHashCode(T value);

    protected abstract String getStringName(T value);

    @Override
    public int hashCode() {
        return toHashCode(value);
    }

    @Override
    public String toString() {
        return getStringName(value);
    }
}
