package com.drk.tools.gplannersample;

import com.drk.tools.gplannercore.annotations.Operator;
import com.drk.tools.gplannercore.core.Atom;
import com.drk.tools.gplannercore.core.main.Operators;
import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannercore.planner.state.GStateTransition;
import com.drk.tools.gplannersample.vars.*;

public class Monkey extends Operators {

    public static At at = new At();
    public static BoxAt boxAt = new BoxAt();
    public static BananasAt bananasAt = new BananasAt();
    public static Have have = new Have();
    public static Level level = new Level();


    @Operator
    StateTransition climbDown(Location x) {
        return new GStateTransition()
                .check(at, x).check(boxAt, x).check(level, LevelStateRange.HIGH)
                .not(level, LevelStateRange.LOW)
                .set(level, LevelStateRange.HIGH);
    }

    @Operator
    StateTransition climbUp(Location x) {
        return new GStateTransition()
                .check(at, x).check(boxAt, x).check(level, LevelStateRange.LOW)
                .not(level, LevelStateRange.LOW)
                .set(level, LevelStateRange.HIGH);
    }

    @Operator
    StateTransition moveBox(Location x, Location y) {
        return new GStateTransition()
                .check(at, x).check(boxAt, x).check(level, LevelStateRange.LOW)
                .not(at, x).not(boxAt, x)
                .set(at, y).set(boxAt, y);
    }

    @Operator
    StateTransition move(Location x, Location y) {
        return new GStateTransition()
                .check(at, x).check(level, LevelStateRange.LOW)
                .not(at, x)
                .set(at, y);
    }

    @Operator
    StateTransition takeBananas(Location x) {
        return new GStateTransition()
                .check(at, x).check(bananasAt, x).check(level, LevelStateRange.HIGH)
                .not(have, ThingRange.NOTHING)
                .set(have, ThingRange.BANANAS);
    }


    public static class At extends Atom<Location> {
    }

    public static class BoxAt extends Atom<Location> {
    }

    public static class BananasAt extends Atom<Location> {
    }

    public static class Have extends Atom<Thing> {
    }

    public static class Level extends Atom<LevelState> {
    }
}
