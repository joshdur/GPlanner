package com.drk.tools.gplannercore.core.state;

import java.util.HashSet;
import java.util.Set;

public abstract class StateTransition {

    private final Set<Statement> preconditions = new HashSet<>();
    private final Set<Statement> effects = new HashSet<>();

    public Set<Statement> getPreconditions() {
        return preconditions;
    }

    public Set<Statement> getEffects() {
        return effects;
    }

    public void check(Statement sStatement) {
        preconditions.add(sStatement);
    }

    public void checkAll(Set<Statement> sStatements) {
        for (Statement statement : sStatements) {
            check(statement);
        }
    }

    public void apply(Statement statement) {
        effects.remove(statement.not());
        effects.add(statement);
    }

    public void applyAll(Set<Statement> sStatements) {
        for (Statement statement : sStatements) {
            apply(statement);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StateTransition && hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        int hashCode1 = preconditions.hashCode();
        int hashCode2 = effects.hashCode();
        return (7 * hashCode1) + (17 * hashCode2);
    }
}
