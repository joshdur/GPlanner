package com.drk.tools.gplannersample.ops;

import com.drk.tools.gplannercore.annotations.Operator;
import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.planner.state.atoms.Atom;
import com.drk.tools.gplannercore.core.main.BaseOperators;
import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannercore.planner.state.GStateTransition;
import com.drk.tools.gplannercore.planner.state.atoms.single.None;
import com.drk.tools.gplannercore.planner.state.atoms.single.SingleAtom;
import com.drk.tools.gplannersample.vars.*;

public class Monkey extends BaseOperators {

    public static At at = new At();
    public static BoxAt boxAt = new BoxAt();
    public static BananasAt bananasAt = new BananasAt();
    public static Have have = new Have();
    public static Level level = new Level();
    public static Test test = new Test();

    public Monkey(Context context) {
        super(context);
    }


    @Operator
    StateTransition climbDown(Location x) {
        return new GStateTransition(context)
                .check(at, x).check(boxAt, x).check(test)
                .check(level, LevelStateRange.HIGH)
                .setAs(level, LevelStateRange.LOW);
    }

    @Operator
    StateTransition climbUp(Location x) {
        return new GStateTransition(context)
                .check(at, x).check(boxAt, x)
                .check(level, LevelStateRange.LOW)
                .setAs(level, LevelStateRange.HIGH);
    }

    @Operator
    StateTransition moveBox(Location x, Location y) {
        return new GStateTransition(context)
                .check(at, x).check(boxAt, x)
                .check(level, LevelStateRange.LOW)
                .setAs(at, y)
                .setAs(boxAt, y);
    }

    @Operator
    StateTransition move(Location x, Location y) {
        return new GStateTransition(context)
                .check(at, x).check(level, LevelStateRange.LOW)
                .setAs(at, y);
    }

    @Operator
    StateTransition takeBananas(Location x) {
        return new GStateTransition(context)
                .check(at, x).check(bananasAt, x)
                .check(level, LevelStateRange.HIGH)
                .setAs(have, ThingRange.BANANAS);
    }

    public static class Test implements SingleAtom{
    }

    public static class At implements Atom<Location> {
    }

    public static class BoxAt implements Atom<Location> {
    }

    public static class BananasAt implements Atom<Location> {
    }

    public static class Have implements Atom<Thing> {
    }

    public static class Level implements Atom<LevelState> {
    }
}
