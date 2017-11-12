package com.drk.tools.gplannercore.core.variables;

public interface VariableRange<T extends Variable> extends Iterable<T>{

    int count();

    T variableAt(int position);


}
