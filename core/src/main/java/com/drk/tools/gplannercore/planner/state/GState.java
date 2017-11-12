package com.drk.tools.gplannercore.planner.state;

import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.state.Statement;

import java.util.HashSet;
import java.util.Set;

public class GState implements State {

    private final Set<Statement> statements;

    public GState(Set<Statement> statements) {
        this.statements = new HashSet<>(statements);
    }

    public GState() {
        this(new HashSet<Statement>());
    }

    @Override
    public State apply(Statement statement) {
        statements.remove(statement.not());
        statements.add(statement);
        return this;
    }

    @Override
    public State applyAll(Set<Statement> statements) {
        for (Statement statement : statements) {
            apply(statement);
        }
        return this;
    }

    @Override
    public boolean check(Statement statement) {
        boolean isContained = statements.contains(statement);
        return isContained || statement.isNegated();
    }

    @Override
    public boolean checkAll(Set<Statement> statements) {
        for (Statement statement : statements) {
            if (!check(statement)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Set<Statement> getStatements() {
        return statements;
    }

    @Override
    public State clone() {
        return new GState(statements);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GState) {
            GState otherState = (GState) obj;
            boolean isEquals = statements.size() == otherState.statements.size();
            return isEquals && statements.containsAll(otherState.statements);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return statements.hashCode();
    }

    @Override
    public String toString() {
        return statements.toString();
    }
}
