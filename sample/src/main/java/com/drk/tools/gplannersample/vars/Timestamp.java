package com.drk.tools.gplannersample.vars;

import com.drk.tools.gplannercore.annotations.variables.NumericRange;
import com.drk.tools.gplannercore.core.variables.numeric.NumericVariable;
import com.drk.tools.gplannercore.core.variables.numeric.NumericVariableRange;

@NumericRange(start = 9, end = 19)
public class Timestamp extends NumericVariable {

    public Timestamp(Integer value, NumericVariableRange<?> numericVariableRange) {
        super(value, numericVariableRange);
    }
}
