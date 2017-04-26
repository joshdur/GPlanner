package com.drk.tools.gplannercore.core.state;

import com.drk.tools.gplannercore.core.Atom;

import java.util.Collection;
import java.util.Set;

public interface StateTransition {

    Set<Statement> getPositivePreconditions();

    Set<Statement> getNegativePreconditions();

    Set<Statement> getPositiveEffects();

    Set<Statement> getNegativeEffects();

    <E extends Enum> StateTransition check(Atom<E> a, E v);

    StateTransition check(Statement sStatement);

    StateTransition checkAll(Collection<Statement> sStatements);

    <E extends Enum> StateTransition set(Atom<E> a, E v);

    StateTransition set(Statement sStatement);

    StateTransition setAll(Collection<Statement> sStatements);

    <E extends Enum> StateTransition not(Atom<E> a, E v);

    StateTransition not(Statement sStatement);

    StateTransition notAll(Collection<Statement> sStatements);

    @Override
    boolean equals(Object object);

    @Override
    int hashCode();

}
