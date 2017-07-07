package com.drk.tools.gplannercore.planner.state.debug;

import com.drk.tools.gplannercore.core.Atom;
import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.core.variables.Variable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DebugStateTransition implements StateTransition {


    private final Set<Statement> positivePreconditions = new HashSet<>();
    private final Set<Statement> negativePreconditions = new HashSet<>();
    private final Set<Statement> positiveEffects = new HashSet<>();
    private final Set<Statement> negativeEffects = new HashSet<>();

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

    public <V extends Variable> DebugStateTransition check(Atom<V> a, V v) {
        return check(DebugStatement.from(a, v));
    }

    public DebugStateTransition check(Statement statement) {
        positivePreconditions.add(statement);
        return this;
    }

    public DebugStateTransition checkAll(Collection<Statement> statements) {
        positivePreconditions.addAll(statements);
        return this;
    }

    public <V extends Variable> DebugStateTransition set(Atom<V> a, V v) {
        return set(DebugStatement.from(a, v));
    }

    public DebugStateTransition set(Statement statement) {
        positiveEffects.add(statement);
        return this;
    }

    public DebugStateTransition setAll(Collection<Statement> statements) {
        positiveEffects.addAll(statements);
        return this;
    }

    public <V extends Variable> DebugStateTransition not(Atom<V> a, V v) {
        return not(DebugStatement.from(a, v));
    }

    public DebugStateTransition not(Statement statement) {
        negativeEffects.add(statement);
        return this;
    }

    public DebugStateTransition notAll(Collection<Statement> statements) {
        negativeEffects.addAll(statements);
        return this;
    }
}
