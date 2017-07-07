package com.drk.tools.gplannercore.core.variables.enumvars;

import com.drk.tools.gplannercore.core.variables.VariableRange;

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
        return valueOf(values[position]);
    }

    protected abstract T valueOf(Enum enumVar);
}
