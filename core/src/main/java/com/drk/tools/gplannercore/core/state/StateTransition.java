package com.drk.tools.gplannercore.core.state;

import java.util.Set;

public interface StateTransition {

    Set<Statement> getPositivePreconditions();

    Set<Statement> getNegativePreconditions();

    Set<Statement> getPositiveEffects();

    Set<Statement> getNegativeEffects();

    @Override
    boolean equals(Object object);

    @Override
    int hashCode();

}
