package com.drk.tools.gplannercore.core.variables.enumvars;

import com.drk.tools.gplannercore.core.variables.VariableIterator;
import com.drk.tools.gplannercore.core.variables.VariableRange;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public abstract class EnumVariableRange<T extends EnumVariable> implements VariableRange<T> {

    private final Enum[] values;

    public EnumVariableRange(Enum[] values) {
        this.values = values;
    }

    @Override
    public int count() {
        return values.length;
    }

    @Override
    public T variableAt(int position) {
        return variableOf(values[position]);
    }

    protected abstract T variableOf(Enum enumVar);

    @Override
    @NotNull
    public Iterator<T> iterator(){
        return new VariableIterator<>(this);
    }
}
