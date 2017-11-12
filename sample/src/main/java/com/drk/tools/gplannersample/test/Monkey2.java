package com.drk.tools.gplannersample.test;

import com.drk.tools.gplannercore.annotations.Operator;
import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.main.BaseOperators;
import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannercore.planner.state.GStateTransition;
import com.drk.tools.gplannersample.ops.Monkey;
import com.drk.tools.gplannersample.vars.LevelStateRange;
import com.drk.tools.gplannersample.vars.Location;

public class Monkey2  extends BaseOperators{

    public static Monkey.At at = new Monkey.At();
    public static Monkey.BoxAt boxAt = new Monkey.BoxAt();
    public static Monkey.BananasAt bananasAt = new Monkey.BananasAt();
    public static Monkey.Have have = new Monkey.Have();
    public static Monkey.Level level = new Monkey.Level();
    public static Monkey.Test test = new Monkey.Test();

    public Monkey2(Context context) {
        super(context);
    }


    @Operator
    StateTransition climbDown2(Location x) {
        return new GStateTransition(context)
                .check(at, x).check(boxAt, x).check(test)
                .check(level, LevelStateRange.HIGH)
                .setAs(level, LevelStateRange.LOW);
    }
}
