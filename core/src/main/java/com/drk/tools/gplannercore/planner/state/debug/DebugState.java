package com.drk.tools.gplannercore.planner.state.debug;

import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.state.Statement;

import java.util.HashSet;
import java.util.Set;

public class DebugState implements State {

    private final Set<Statement> statements;

    public DebugState(Set<Statement> statements) {
        this.statements = new HashSet<>(statements);
    }

    public DebugState() {
        this(new HashSet<Statement>());
    }

    @Override
    public State remove(Statement statement) {
        statements.remove(statement);
        return this;
    }

    @Override
    public State set(Statement statement) {
        statements.add(statement);
        return this;
    }

    @Override
    public boolean check(Statement statement) {
        return statements.contains(statement);
    }

    @Override
    public boolean checkNot(Statement statement) {
        return !statements.contains(statement);
    }

    @Override
    public Set<Statement> getStatements() {
        return statements;
    }

    @Override
    public State clone() {
        return new DebugState(statements);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DebugState) {
            DebugState otherState = (DebugState) obj;
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
