package com.drk.tools.gplannercore.core.variables;

public interface VariableRange<T extends Variable> {

    int count();

    T variableAt(int position);


}
