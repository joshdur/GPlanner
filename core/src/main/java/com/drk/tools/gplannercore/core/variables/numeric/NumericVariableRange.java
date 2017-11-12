package com.drk.tools.gplannercore.core.variables.numeric;

import com.drk.tools.gplannercore.core.variables.VariableIterator;
import com.drk.tools.gplannercore.core.variables.VariableRange;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public abstract class NumericVariableRange<T extends NumericVariable> implements VariableRange<T> {

    private final int start;
    private final int end;

    public NumericVariableRange(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public int count() {
        return Math.abs(end - start);
    }

    @Override
    public T variableAt(int position) {
        int value = start + position;
        assertRange(value);
        return variableOf(value);
    }

    protected abstract T variableOf(int value);

    public int getPositionOfValue(int value) {
        assertRange(value);
        return value - start;
    }

    private void assertRange(int value) {
        if (!isInRange(value)) {
            throw new IndexOutOfBoundsException();
        }
    }

    private boolean isInRange(int value) {
        return value >= start && value <= end;
    }

    @Override
    @NotNull
    public Iterator<T> iterator(){
        return new VariableIterator<>(this);
    }
}
