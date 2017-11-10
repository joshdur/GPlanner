package com.drk.tools.gplannercore.planner.state;

import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.core.variables.Variable;
import com.drk.tools.gplannercore.planner.state.atoms.Atom;
import com.drk.tools.gplannercore.planner.state.atoms.BinaryAtom;
import com.drk.tools.gplannercore.planner.state.atoms.TernaryAtom;
import com.drk.tools.gplannercore.planner.state.atoms.single.None;

import java.util.Collection;

public class GStateTransition extends StateTransition {

    private final Context context;

    public GStateTransition(Context context) {
        this.context = context;
    }

    public GStateTransition check(Atom<None> atom) {
        return check(atom, None.NONE);
    }

    public <V extends Variable> GStateTransition check(Atom<V> a, V v) {
        check(StatementBuilder.build(context.isDebug(), a, v));
        return this;
    }

    public <V1 extends Variable, V2 extends Variable> GStateTransition check(BinaryAtom<V1, V2> a, V1 v1, V2 v2) {
        check(StatementBuilder.build(context.isDebug(), a, v1, v2));
        return this;
    }

    public <V1 extends Variable, V2 extends Variable, V3 extends Variable> GStateTransition check(TernaryAtom<V1, V2, V3> a, V1 v1, V2 v2, V3 v3) {
        check(StatementBuilder.build(context.isDebug(), a, v1, v2, v3));
        return this;
    }

    public GStateTransition checkNot(Atom<None> atom) {
        return checkNot(atom, None.NONE);
    }

    public <V extends Variable> GStateTransition checkNot(Atom<V> a, V v) {
        check(StatementBuilder.build(context.isDebug(), a, v).not());
        return this;
    }

    public <V1 extends Variable, V2 extends Variable> GStateTransition checkNot(BinaryAtom<V1, V2> a, V1 v1, V2 v2) {
        check(StatementBuilder.build(context.isDebug(), a, v1, v2).not());
        return this;
    }

    public <V1 extends Variable, V2 extends Variable, V3 extends Variable> GStateTransition checkNot(TernaryAtom<V1, V2, V3> a, V1 v1, V2 v2, V3 v3) {
        check(StatementBuilder.build(context.isDebug(), a, v1, v2, v3).not());
        return this;
    }

    public GStateTransition set(Atom<None> atom) {
        return set(atom, None.NONE);
    }

    public <V extends Variable> GStateTransition set(Atom<V> a, V v) {
        apply(StatementBuilder.build(context.isDebug(), a, v));
        return this;
    }

    public <V1 extends Variable, V2 extends Variable> GStateTransition set(BinaryAtom<V1, V2> a, V1 v1, V2 v2) {
        apply(StatementBuilder.build(context.isDebug(), a, v1, v2));
        return this;
    }

    public <V1 extends Variable, V2 extends Variable, V3 extends Variable> GStateTransition set(TernaryAtom<V1, V2, V3> a, V1 v1, V2 v2, V3 v3) {
        apply(StatementBuilder.build(context.isDebug(), a, v1, v2, v3));
        return this;
    }

    public <V extends Variable> GStateTransition setAs(Atom<V> a, V v) {
        apply(StatementBuilder.build(context.isDebug(), a, v));
        return notAll(StatementBuilder.buildOthers(context, a, v));
    }

    public <V1 extends Variable, V2 extends Variable> GStateTransition setAs(BinaryAtom<V1, V2> a, V1 v1, V2 v2) {
        apply(StatementBuilder.build(context.isDebug(), a, v1, v2));
        return notAll(StatementBuilder.buildOthers(context, a, v1, v2));
    }

    public <V1 extends Variable, V2 extends Variable, V3 extends Variable> GStateTransition setAs(TernaryAtom<V1, V2, V3> a, V1 v1, V2 v2, V3 v3) {
        apply(StatementBuilder.build(context.isDebug(), a, v1, v2, v3));
        return notAll(StatementBuilder.buildOthers(context, a, v1, v2, v3));
    }

    public GStateTransition not(Atom<None> atom) {
        return not(atom, None.NONE);
    }

    public <V extends Variable> GStateTransition not(Atom<V> a, V v) {
        apply(StatementBuilder.build(context.isDebug(), a, v).not());
        return this;
    }

    public <V1 extends Variable, V2 extends Variable> GStateTransition not(BinaryAtom<V1, V2> a, V1 v1, V2 v2) {
        apply(StatementBuilder.build(context.isDebug(), a, v1, v2).not());
        return this;
    }

    public <V1 extends Variable, V2 extends Variable, V3 extends Variable> GStateTransition not(TernaryAtom<V1, V2, V3> a, V1 v1, V2 v2, V3 v3) {
        apply(StatementBuilder.build(context.isDebug(), a, v1, v2, v3).not());
        return this;
    }

    public GStateTransition notAll(Collection<Statement> statements) {
        for (Statement statement : statements) {
            apply(statement.not());
        }
        return this;
    }
}
