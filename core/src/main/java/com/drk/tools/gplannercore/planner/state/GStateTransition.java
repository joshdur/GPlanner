package com.drk.tools.gplannercore.planner.state;

import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.core.variables.Variable;
import com.drk.tools.gplannercore.planner.state.atoms.Atom;
import com.drk.tools.gplannercore.planner.state.atoms.BinaryAtom;
import com.drk.tools.gplannercore.planner.state.atoms.TernaryAtom;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class GStateTransition implements StateTransition {

    private final Set<Statement> positivePreconditions = new HashSet<>();
    private final Set<Statement> negativePreconditions = new HashSet<>();
    private final Set<Statement> positiveEffects = new HashSet<>();
    private final Set<Statement> negativeEffects = new HashSet<>();
    private final Context context;

    public GStateTransition(Context context) {
        this.context = context;
    }


    @Override
    public Set<Statement> getPositivePreconditions() {
        return positivePreconditions;
    }

    @Override
    public Set<Statement> getNegativePreconditions() {
        return negativePreconditions;
    }

    @Override
    public Set<Statement> getPositiveEffects() {
        return positiveEffects;
    }

    @Override
    public Set<Statement> getNegativeEffects() {
        return negativeEffects;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StateTransition && hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        int hashCode1 = positivePreconditions.hashCode();
        int hashCode2 = negativePreconditions.hashCode();
        int hashCode3 = positiveEffects.hashCode();
        int hashCode4 = negativeEffects.hashCode();
        return (7 * hashCode1) + (17 * hashCode2) + (31 * hashCode3) + (43 * hashCode4);
    }

    public <V extends Variable> GStateTransition check(Atom<V> a, V v) {
        return check(StatementBuilder.build(context.isDebug(), a, v));
    }

    public <V1 extends Variable, V2 extends Variable> GStateTransition check(BinaryAtom<V1, V2> a, V1 v1, V2 v2) {
        return check(StatementBuilder.build(context.isDebug(), a, v1, v2));
    }

    public <V1 extends Variable, V2 extends Variable, V3 extends Variable> GStateTransition check(TernaryAtom<V1, V2, V3> a, V1 v1, V2 v2, V3 v3) {
        return check(StatementBuilder.build(context.isDebug(), a, v1, v2, v3));
    }

    public GStateTransition check(Statement statement) {
        positivePreconditions.add(statement);
        return this;
    }

    public GStateTransition checkAll(Collection<Statement> statements) {
        positivePreconditions.addAll(statements);
        return this;
    }

    public <V extends Variable> GStateTransition set(Atom<V> a, V v) {
        return set(StatementBuilder.build(context.isDebug(), a, v));
    }

    public <V1 extends Variable, V2 extends Variable> GStateTransition set(BinaryAtom<V1, V2> a, V1 v1, V2 v2) {
        return set(StatementBuilder.build(context.isDebug(), a, v1, v2));
    }

    public <V1 extends Variable, V2 extends Variable, V3 extends Variable> GStateTransition set(TernaryAtom<V1, V2, V3> a, V1 v1, V2 v2, V3 v3) {
        return set(StatementBuilder.build(context.isDebug(), a, v1, v2, v3));
    }

    public GStateTransition set(Statement statement) {
        positiveEffects.add(statement);
        return this;
    }

    public <V extends Variable> GStateTransition setAs(Atom<V> a, V v) {
        set(StatementBuilder.build(context.isDebug(), a, v));
        return notAll(StatementBuilder.buildOthers(context, a, v));
    }

    public <V1 extends Variable, V2 extends Variable> GStateTransition setAs(BinaryAtom<V1, V2> a, V1 v1, V2 v2) {
        set(StatementBuilder.build(context.isDebug(), a, v1, v2));
        return notAll(StatementBuilder.buildOthers(context, a, v1, v2));
    }

    public <V1 extends Variable, V2 extends Variable, V3 extends Variable> GStateTransition setAs(TernaryAtom<V1, V2, V3> a, V1 v1, V2 v2, V3 v3) {
        set(StatementBuilder.build(context.isDebug(), a, v1, v2, v3));
        return notAll(StatementBuilder.buildOthers(context, a, v1, v2, v3));
    }

    public GStateTransition setAll(Collection<Statement> statements) {
        positiveEffects.addAll(statements);
        return this;
    }

    public <V extends Variable> GStateTransition not(Atom<V> a, V v) {
        return not(StatementBuilder.build(context.isDebug(), a, v));
    }

    public <V1 extends Variable, V2 extends Variable> GStateTransition not(BinaryAtom<V1, V2> a, V1 v1, V2 v2) {
        return not(StatementBuilder.build(context.isDebug(), a, v1, v2));
    }

    public <V1 extends Variable, V2 extends Variable, V3 extends Variable> GStateTransition not(TernaryAtom<V1, V2, V3> a, V1 v1, V2 v2, V3 v3) {
        return not(StatementBuilder.build(context.isDebug(), a, v1, v2, v3));
    }

    public GStateTransition not(Statement statement) {
        negativeEffects.add(statement);
        return this;
    }

    public GStateTransition notAll(Collection<Statement> statements) {
        negativeEffects.addAll(statements);
        return this;
    }
}
