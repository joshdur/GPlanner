package com.drk.tools.gplannercore.core.state;

import java.util.Set;

public interface State {

    State remove(Statement statement);

    State set(Statement statement);

    boolean check(Statement statement);

    boolean checkNot(Statement statement);

    Set<Statement> getStatements();

    @Override
    boolean equals(Object object);

    @Override
    int hashCode();

}
