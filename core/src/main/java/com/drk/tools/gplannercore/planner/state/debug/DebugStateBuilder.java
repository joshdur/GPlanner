package com.drk.tools.gplannercore.planner.state.debug;

import com.drk.tools.gplannercore.core.Atom;
import com.drk.tools.gplannercore.core.state.StateBuilder;

public class DebugStateBuilder implements StateBuilder {

    private final DebugState state;

    public DebugStateBuilder() {
        this.state = new DebugState();
    }

    public <E extends Enum> DebugStateBuilder set(Atom<E> a, E v) {
        state.set(DebugStatement.from(a, v));
        return this;
    }

    public DebugState build() {
        return state;
    }
}
