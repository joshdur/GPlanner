package com.drk.tools.gplannercore.core.variables.numeric;

import com.drk.tools.gplannercore.core.variables.Variable;

public class NumericVariable extends Variable<Integer> {

    private final NumericVariableRange<?> numericVariableRange;

    public NumericVariable(Integer value, NumericVariableRange<?> numericVariableRange) {
        super(value);
        this.numericVariableRange = numericVariableRange;
    }

    public NumericVariable sum(NumericVariable other) {
        return variable(value + other.value);
    }

    public NumericVariable sus(NumericVariable other) {
        return variable(value - other.value);
    }

    public NumericVariable add(int number) {
        return variable(value + number);
    }

    public NumericVariable sus(int number) {
        return variable(value - number);
    }

    private NumericVariable variable(int newValue) {
        int position = numericVariableRange.getPositionOfValue(newValue);
        return numericVariableRange.variableAt(position);
    }
}
