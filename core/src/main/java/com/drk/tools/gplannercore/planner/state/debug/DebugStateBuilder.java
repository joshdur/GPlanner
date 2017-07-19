package com.drk.tools.gplannercore.planner.state.debug;

import com.drk.tools.gplannercore.core.atoms.Atom;
import com.drk.tools.gplannercore.core.atoms.BinaryAtom;
import com.drk.tools.gplannercore.core.atoms.TernaryAtom;
import com.drk.tools.gplannercore.core.variables.Variable;
import com.drk.tools.gplannercore.planner.state.GStateBuilder;
import com.drk.tools.gplannercore.planner.state.GStatement;

public class DebugStateBuilder {

    private final DebugState state;

    public DebugStateBuilder() {
        this.state = new DebugState();
    }

    public <V extends Variable> DebugStateBuilder set(Atom<V> a, V v) {
        state.set(DebugStatement.from(a, v));
        return this;
    }

    public <V1 extends Variable, V2 extends Variable> DebugStateBuilder from(BinaryAtom<V1, V2> atom, V1 v1, V2 v2) {
        state.set(GStatement.from(atom, v1, v2));
        return this;
    }

    public <V1 extends Variable, V2 extends Variable, V3 extends Variable> DebugStateBuilder from(TernaryAtom<V1, V2, V3> atom, V1 v1, V2 v2, V3 v3) {
        state.set(GStatement.from(atom, v1, v2, v3));
        return this;
    }

    public DebugState build() {
        return state;
    }
}
