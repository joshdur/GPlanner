package com.drk.tools.gplannercore.planner.search.unifier;

import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.core.state.Transition;

import java.util.Set;

 public abstract class SearchUnifier {

    private final Set<Statement> statements;

    public SearchUnifier(Set<Statement> statements) {
        this.statements = statements;
    }

    public abstract Transition next();

    protected boolean isApplicable(Transition transition){
        StateTransition stateTransition = transition.stateTransition;
        boolean isApplicable = statements.containsAll(stateTransition.getPositivePreconditions());
        return isApplicable && statementsNonContainsAny(stateTransition.getNegativePreconditions());
    }

    private boolean statementsNonContainsAny(Set<Statement> negativePreconditions){
        for(Statement statement : negativePreconditions){
            if(statements.contains(statement)){
                return false;
            }
        }
        return true;
    }

}
