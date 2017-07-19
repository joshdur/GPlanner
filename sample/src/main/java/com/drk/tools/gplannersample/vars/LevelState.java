package com.drk.tools.gplannersample.vars;

import com.drk.tools.gplannercore.annotations.variables.EnumRange;
import com.drk.tools.gplannercore.core.variables.enumvars.EnumVariable;

@EnumRange(enumClass = LevelState.LevelStatePos.class)
public class LevelState extends EnumVariable {
    public LevelState(Enum value) {
        super(value);
    }

    public enum LevelStatePos {
        HIGH, LOW
    }
}
