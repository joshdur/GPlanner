package com.drk.tools.gplannercore.planner.state.debug;

import com.drk.tools.gplannercore.core.Atom;
import com.drk.tools.gplannercore.core.variables.Variable;

public class DebugStateBuilder {

    private final DebugState state;

    public DebugStateBuilder() {
        this.state = new DebugState();
    }

    public <V extends Variable> DebugStateBuilder set(Atom<V> a, V v) {
        state.set(DebugStatement.from(a, v));
        return this;
    }

    public DebugState build() {
        return state;
    }
}
