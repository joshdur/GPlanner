package com.drk.tools.gplannercore.planner.search.graphplan;

import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.core.state.Transition;

import java.util.*;

class Extraction {

    static List<Set<Transition>> extract(GraphPlan.Layer layer, State finalState){
        List<Set<Transition>> layeredPlan = new ArrayList<>();
        GraphPlan.Layer currentLayer = layer;
        Set<Statement> preconds = finalState.getStatements();
        while(currentLayer != null){
            Set<Transition> transitions = extractRelevantTransition(currentLayer, preconds);
            layeredPlan.add(transitions);
            preconds = allPreconds(transitions);
            currentLayer = currentLayer.lastLayer;
        }
        Collections.reverse(layeredPlan);
        return layeredPlan;
    }

    private static Set<Statement> allPreconds(Set<Transition> transitions){
        Set<Statement> preconds = new HashSet<>();
        for(Transition transition : transitions){
            preconds.addAll(transition.stateTransition.getPositivePreconditions());
        }
        return preconds;
    }

    private static Set<Transition> extractRelevantTransition(GraphPlan.Layer layer, Set<Statement> preconds){
        Set<Transition> relevant = new HashSet<>();
        for(Statement statement : preconds){
            for(Transition transition : layer.applicableTransitions){
                if(transition.stateTransition.getPositiveEffects().contains(statement)){
                    relevant.add(transition);
                }
            }
        }
        return relevant;
    }

    static boolean reachedGoal(GraphPlan.Layer layer, State finalState) {
        Set<Statement> goalStatements = finalState.getStatements();
        if (!layer.statements.containsAll(goalStatements)) {
            return false;
        }
        Set<GraphPlan.Rel> stExclusion = layer.statementExclusions;
        for (Statement one : goalStatements) {
            for (Statement other : goalStatements) {
                if (!one.equals(other)) {
                    if(stExclusion.contains(new GraphPlan.Rel(one, other))){
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
