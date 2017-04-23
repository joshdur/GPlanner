package com.drk.tools.gplannercore.planner.state;

import com.drk.tools.gplannercore.core.Atom;

public class GStateBuilder {

    private final GState state;

    public GStateBuilder() {
        this.state = new GState();
    }

    public <E extends Enum> GStateBuilder set(Atom<E> a, E v) {
        state.set(GStatement.from(a, v));
        return this;
    }

    public GState build(){
        return state;
    }
}
