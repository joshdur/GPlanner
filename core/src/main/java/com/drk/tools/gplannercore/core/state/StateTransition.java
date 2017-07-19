package com.drk.tools.gplannercore.core.state;

import java.util.Collection;
import java.util.Set;

public interface StateTransition {

    Set<Statement> getPositivePreconditions();

    Set<Statement> getNegativePreconditions();

    Set<Statement> getPositiveEffects();

    Set<Statement> getNegativeEffects();

    StateTransition check(Statement sStatement);

    StateTransition checkAll(Collection<Statement> sStatements);

    StateTransition set(Statement sStatement);

    StateTransition setAll(Collection<Statement> sStatements);

    StateTransition not(Statement sStatement);

    StateTransition notAll(Collection<Statement> sStatements);

    @Override
    boolean equals(Object object);

    @Override
    int hashCode();

}
