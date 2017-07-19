package com.drk.tools.gplannercore.core.main;

import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannercore.core.state.Statement;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class EmptyTransition implements StateTransition {

    @Override
    public Set<Statement> getPositivePreconditions() {
        return Collections.emptySet();
    }

    @Override
    public Set<Statement> getNegativePreconditions() {
        return Collections.emptySet();
    }

    @Override
    public Set<Statement> getPositiveEffects() {
        return Collections.emptySet();
    }

    @Override
    public Set<Statement> getNegativeEffects() {
        return Collections.emptySet();
    }

    @Override
    public StateTransition check(Statement sStatement) {
        return this;
    }

    @Override
    public StateTransition checkAll(Collection<Statement> sStatements) {
        return this;
    }

    @Override
    public StateTransition set(Statement sStatement) {
        return this;
    }

    @Override
    public StateTransition setAll(Collection<Statement> sStatements) {
        return this;
    }

    @Override
    public StateTransition not(Statement sStatement) {
        return this;
    }

    @Override
    public StateTransition notAll(Collection<Statement> sStatements) {
        return this;
    }
}
