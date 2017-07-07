package com.drk.tools.gplannercore;

import com.drk.tools.gplannercore.annotations.Operator;
import com.drk.tools.gplannercore.annotations.variables.EnumRange;
import com.drk.tools.gplannercore.core.Atom;
import com.drk.tools.gplannercore.core.main.Operators;
import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannercore.core.variables.enumvars.EnumVariable;
import com.drk.tools.gplannercore.planner.state.GStateTransition;

public class Monkey extends Operators {


    public static At at = new At();
    public static BoxAt boxAt = new BoxAt();
    public static BananasAt bananasAt = new BananasAt();
    public static Have have = new Have();
    public static Level level = new Level();


    @Operator
    public StateTransition climbDown(Location x) {
        return new GStateTransition()
                .check(at, x).check(boxAt, x).check(level, LevelState.HIGH)
                .not(level, LevelState.LOW)
                .set(level, LevelState.HIGH);
    }

    @Operator
    public StateTransition climbUp(Location x) {
        return new GStateTransition()
                .check(at, x).check(boxAt, x).check(level, LevelState.LOW)
                .not(level, LevelState.LOW)
                .set(level, LevelState.HIGH);
    }

    @Operator
    public StateTransition moveBox(Location x, Location y) {
        return new GStateTransition()
                .check(at, x).check(boxAt, x).check(level, LevelState.LOW)
                .not(at, x).not(boxAt, x)
                .set(at, y).set(boxAt, y);
    }

    @Operator
    public StateTransition move(Location x, Location y) {
        return new GStateTransition()
                .check(at, x).check(level, LevelState.LOW)
                .not(at, x)
                .set(at, y);
    }

    @Operator
    public StateTransition takeBananas(Location x) {
        return new GStateTransition()
                .check(at, x).check(bananasAt, x).check(level, LevelState.HIGH)
                .not(have, Thing.NOTHING)
                .set(have, Thing.BANANAS);
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

    @EnumRange(enumClass = Monkey.Location.Loc.class)
    public static class Location extends EnumVariable {

        public Location(Enum value) {
            super(value);
        }

        public enum Loc {
            A, B, C, D, E
        }
    }

    @EnumRange(enumClass = Thing.ThingEnum.class)
    public static class Thing extends EnumVariable {

        public Thing(Enum value) {
            super(value);
        }

        public enum ThingEnum {
            NOTHING, BANANAS
        }
    }


    @EnumRange(enumClass = LevelState.LevelStateEnum.class)
    public static class LevelState extends EnumVariable {

        public LevelState(Enum value) {
            super(value);
        }

        public enum LevelStateEnum {
            HIGH, LOW
        }
    }


}
