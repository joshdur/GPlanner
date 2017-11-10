package com.drk.tools.gplannercore.planner.state;

import com.drk.tools.gplannercore.core.variables.Variable;
import com.drk.tools.gplannercore.planner.state.atoms.Atom;
import com.drk.tools.gplannercore.planner.state.atoms.BinaryAtom;
import com.drk.tools.gplannercore.planner.state.atoms.TernaryAtom;
import com.drk.tools.gplannercore.planner.state.atoms.single.None;

public class GStateBuilder {

    private final GState state;
    private final boolean debug;

    public GStateBuilder() {
        this(false);
    }

    public GStateBuilder(boolean debug) {
        this.state = new GState();
        this.debug = debug;
    }

    public GStateBuilder set(Atom<None> atom){
        return set(atom, None.NONE);
    }

    public <V extends Variable> GStateBuilder set(Atom<V> a, V v) {
        state.apply(StatementBuilder.build(debug, a, v));
        return this;
    }

    public <V1 extends Variable, V2 extends Variable> GStateBuilder set(BinaryAtom<V1, V2> a, V1 v1, V2 v2) {
        state.apply(StatementBuilder.build(debug, a, v1, v2));
        return this;
    }

    public <V1 extends Variable, V2 extends Variable, V3 extends Variable> GStateBuilder set(TernaryAtom<V1, V2, V3> a, V1 v1, V2 v2, V3 v3) {
        state.apply(StatementBuilder.build(debug, a, v1, v2, v3));
        return this;
    }


    public GStateBuilder not(Atom<None> atom){
        return not(atom, None.NONE);
    }

    public <V extends Variable> GStateBuilder not(Atom<V> a, V v) {
        state.apply(StatementBuilder.build(debug, a, v).not());
        return this;
    }

    public <V1 extends Variable, V2 extends Variable> GStateBuilder not(BinaryAtom<V1, V2> a, V1 v1, V2 v2) {
        state.apply(StatementBuilder.build(debug, a, v1, v2).not());
        return this;
    }

    public <V1 extends Variable, V2 extends Variable, V3 extends Variable> GStateBuilder not(TernaryAtom<V1, V2, V3> a, V1 v1, V2 v2, V3 v3) {
        state.apply(StatementBuilder.build(debug, a, v1, v2, v3).not());
        return this;
    }

    public GState build() {
        return state;
    }
}
