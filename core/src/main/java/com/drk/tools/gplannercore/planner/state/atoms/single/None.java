package com.drk.tools.gplannercore.planner.state.atoms.single;

import com.drk.tools.gplannercore.core.variables.enumvars.EnumVariable;
import com.drk.tools.gplannercore.core.variables.enumvars.EnumVariableRange;


public class None extends EnumVariable {

    public static final None NONE = new None(None.NoneEnum.NONE);

    public None(Enum value) {
        super(value);
    }

    public enum NoneEnum {
        NONE
    }

    public static class Range extends EnumVariableRange<None> {

        public Range() {
            super(None.NoneEnum.values());
        }

        protected None variableOf(Enum value) {
            return new None(value);
        }
    }
}
