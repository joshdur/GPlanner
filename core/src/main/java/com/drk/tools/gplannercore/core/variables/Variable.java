package com.drk.tools.gplannercore.core.variables;

public abstract class Variable<T> {

    protected final T value;

    public Variable(T value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(getClass()) && obj.hashCode() == hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
