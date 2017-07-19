package com.drk.tools.gplannercore.planner.state;

import com.drk.tools.gplannercore.core.atoms.Atom;
import com.drk.tools.gplannercore.core.atoms.BinaryAtom;
import com.drk.tools.gplannercore.core.atoms.TernaryAtom;
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

    public <V1 extends Variable, V2 extends Variable> GStateBuilder from(BinaryAtom<V1, V2> atom, V1 v1, V2 v2) {
        state.set(GStatement.from(atom, v1, v2));
        return this;
    }

    public <V1 extends Variable, V2 extends Variable, V3 extends Variable> GStateBuilder from(TernaryAtom<V1, V2, V3> atom, V1 v1, V2 v2, V3 v3) {
        state.set(GStatement.from(atom, v1, v2, v3));
        return this;
    }

    public GState build() {
        return state;
    }
}
