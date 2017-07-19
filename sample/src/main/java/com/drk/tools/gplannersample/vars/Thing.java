package com.drk.tools.gplannersample.vars;

import com.drk.tools.gplannercore.annotations.variables.EnumRange;
import com.drk.tools.gplannercore.core.variables.enumvars.EnumVariable;

@EnumRange(enumClass = Thing.ThingPos.class)
public class Thing extends EnumVariable {

    public Thing(Enum value) {
        super(value);
    }

    public enum ThingPos {
        NOTHING, BANANAS
    }
}
