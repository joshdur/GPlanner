package com.drk.tools.gplannercore.core.variables;

import java.util.Iterator;

public class VariableIterator<T extends Variable> implements Iterator<T> {

    private final VariableRange<T> range;
    private int index;

    public VariableIterator(VariableRange<T> range) {
        this.range = range;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < range.count();
    }

    @Override
    public T next() {
        T element = range.variableAt(index);
        index ++;
        return element;
    }
}
