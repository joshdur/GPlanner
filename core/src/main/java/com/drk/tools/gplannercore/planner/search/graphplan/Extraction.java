package com.drk.tools.gplannercore.planner.search.graphplan;

import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.core.state.Transition;
import com.drk.tools.gplannercore.planner.logger.LogLevel;
import com.drk.tools.gplannercore.planner.logger.Logger;

import java.util.*;

class Extraction {

    private final Context context;
    private final LogLevel logLevel;
    private final Logger logger;

    Extraction(Context context, LogLevel logLevel, Logger logger) {
        this.context = context;
        this.logLevel = logLevel;
        this.logger = logger;
    }

    List<Set<Transition>> extract(GraphPlan.Layer layer, State finalState) {
        List<Set<Transition>> layeredPlan = new ArrayList<>();
        GraphPlan.Layer currentLayer = layer;
        Set<Statement> preconds = finalState.getStatements();
        while (currentLayer != null) {
            if(logLevel.isOver(LogLevel.DEBUG)){
                logger.log("GraphPlan - Extraction", "");
                logger.log("GraphPlan - Extraction", "Looking for these preconditions: ");
                logger.log("GraphPlan - Extraction", "     " + preconds.toString());
                logger.log("GraphPlan - Extraction", "");
                logger.log("GraphPlan - Extraction", "");
                logger.log("Graphplan - Layer", "");
                for (Transition transition : layer.applicableTransitions) {
                    logger.log("Graphplan - Layer", "     Transition: " + (transition.isNoop() ? "NOOP" : context.asString(transition)));
                    logger.log("Graphplan - Layer", "        preconds: " + transition.getPreconditions());
                    logger.log("Graphplan - Layer", "        effects: " + transition.getEffects());
                }
            }
            Set<Transition> transitions = extractRelevantTransition(currentLayer, preconds);

            if(logLevel.isOver(LogLevel.DEBUG)){
                logger.log("Graphplan - Applicable Transitions", "");
                for (Transition transition : layer.applicableTransitions) {
                    logger.log("Graphplan - Applicable", "     Transition: " + (transition.isNoop() ? "NOOP" : context.asString(transition)));
                    logger.log("Graphplan - Applicable", "        preconds: " + transition.getPreconditions());
                    logger.log("Graphplan - Applicable", "        effects: " + transition.getEffects());
                }
            }

            layeredPlan.add(transitions);
            preconds = allPreconds(transitions);
            currentLayer = currentLayer.lastLayer;
        }
        Collections.reverse(layeredPlan);
        return layeredPlan;
    }

    private Set<Statement> allPreconds(Set<Transition> transitions) {
        Set<Statement> preconds = new HashSet<>();
        for (Transition transition : transitions) {
            preconds.addAll(transition.getPreconditions());
        }
        return preconds;
    }

    private Set<Transition> extractRelevantTransition(GraphPlan.Layer layer, Set<Statement> preconds) {
        Set<Transition> relevant = new HashSet<>();
        if (preconds.isEmpty()) {
            relevant.addAll(layer.applicableTransitions);
        } else {
            for (Statement statement : preconds) {
                for (Transition transition : layer.applicableTransitions) {
                    if (transition.getEffects().contains(statement)) {
                        relevant.add(transition);
                    }
                }
            }
        }
        return relevant;
    }

    boolean reachedGoal(GraphPlan.Layer layer, State finalState) {
        Set<Statement> goalStatements = finalState.getStatements();

        for (Statement goal : goalStatements) {
            if (!goal.isNegated() && !layer.statements.contains(goal)) {
                return false;
            }
        }

        Set<GraphPlan.Rel> stExclusion = layer.statementExclusions;
        for (Statement one : goalStatements) {
            for (Statement other : goalStatements) {
                if (!one.equals(other)) {
                    if (stExclusion.contains(new GraphPlan.Rel(one, other))) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

}
