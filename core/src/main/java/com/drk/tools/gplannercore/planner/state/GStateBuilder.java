package com.drk.tools.gplannercore.planner.state;

import com.drk.tools.gplannercore.core.Atom;
import com.drk.tools.gplannercore.core.variables.Variable;

public class GStateBuilder {

    private final GState state;

    public GStateBuilder() {
        this.state = new GState();
    }

    public <V extends Variable> GStateBuilder set(Atom<V> a, V v) {
        state.set(GStatement.from(a, v));
        return this;
    }

    public GState build(){
        return state;
    }
}
