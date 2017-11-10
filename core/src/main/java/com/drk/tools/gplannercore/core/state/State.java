package com.drk.tools.gplannercore.core.state;

import java.util.Collection;
import java.util.Set;

public interface State {

    State apply(Statement statement);

    State applyAll(Set<Statement> statements);

    boolean check(Statement statement);

    boolean checkAll(Set<Statement> statements);

    Set<Statement> getStatements();

    State clone();

    @Override
    boolean equals(Object object);

    @Override
    int hashCode();

}
