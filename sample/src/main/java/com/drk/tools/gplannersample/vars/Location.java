package com.drk.tools.gplannersample.vars;

import com.drk.tools.gplannercore.annotations.variables.EnumRange;
import com.drk.tools.gplannercore.core.variables.enumvars.EnumVariable;

@EnumRange(enumClass = Location.LocationPos.class)
public class Location extends EnumVariable {

    public Location(Enum value) {
        super(value);
    }

    public enum LocationPos {
        A, B, C, D, E
    }
}
