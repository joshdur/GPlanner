package com.drk.tools.gplannercore.planner.search.graphplan;

import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.core.state.Transition;

import java.util.HashSet;
import java.util.Set;

class Mutex {


    static Set<GraphPlan.Rel> mutexStatements(Set<Statement> statements, GraphPlan.Layer layer) {
        Set<Transition> transitions = layer.applicableTransitions;
        Set<GraphPlan.Rel> tExclusion = layer.transitionExclusions;
        Set<GraphPlan.Rel> stExclusions = new HashSet<>();
        for (Statement one : statements) {
            for (Statement other : statements) {
                if (!one.equals(other)) {
                    if (!existsTransitionForTwo(one, other, transitions)) {
                        Set<Transition> withEffectOne = transitionsWithEffect(transitions, one);
                        Set<Transition> withEffectOther = transitionsWithEffect(transitions, other);
                        if (!anyNonExcluded(withEffectOne, withEffectOther, tExclusion)) {
                            stExclusions.add(new GraphPlan.Rel(one, other));
                        }
                    }
                }
            }
        }
        for(Statement one : statements){
            if(statements.contains(one.not())){
                stExclusions.add(new GraphPlan.Rel(one, one.not()));
            }
        }

        return stExclusions;
    }

    private static boolean anyNonExcluded(Set<Transition> one, Set<Transition> other, Set<GraphPlan.Rel> tExclusion) {
        for (Transition o : one) {
            for (Transition ot : other) {
                if (!tExclusion.contains(new GraphPlan.Rel(o, ot))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static Set<Transition> transitionsWithEffect(Set<Transition> transitions, Statement effect) {
        Set<Transition> withEffect = new HashSet<>();
        for (Transition transition : transitions) {
            if (transition.getEffects().contains(effect)) {
                withEffect.add(transition);
            }
        }
        return withEffect;
    }

    private static boolean existsTransitionForTwo(Statement one, Statement other, Set<Transition> transitions) {
        for (Transition transition : transitions) {
            StateTransition stateTransition = transition.stateTransition;
            Set<Statement> effects = stateTransition.getEffects();
            if (effects.contains(one) && effects.contains(other)) {
                return true;
            }
        }
        return false;
    }

    static Set<GraphPlan.Rel> mutexTransitions(Set<Transition> transitions, Set<GraphPlan.Rel> statementExclusions) {
        Set<GraphPlan.Rel> transitionExclusions = new HashSet<>();
        for (Transition one : transitions) {
            for (Transition other : transitions) {
                addMutex(one, other, statementExclusions, transitionExclusions);
            }
        }
        return transitionExclusions;
    }

    private static void addMutex(Transition one, Transition other, Set<GraphPlan.Rel> stExclusion, Set<GraphPlan.Rel> tExclusion) {
        if (one.equals(other)) {
            return;
        }
        if (!areIndependent(one, other, stExclusion)) {
            tExclusion.add(new GraphPlan.Rel(one, other));
        }
    }

    private static boolean areIndependent(Transition one, Transition other, Set<GraphPlan.Rel> stExclusion) {
        return checkEmptyMutexPreconditions(one, other, stExclusion)
                && checkIndependence(one, other)
                && checkIndependence(other, one);
    }

    private static boolean checkIndependence(Transition one, Transition toOther) {
        Set<Statement> join = new HashSet<>();
        join.addAll(filter(toOther.getPreconditions(), false));
        join.addAll(filter(toOther.getEffects(),false));
        Set<Statement> negativeEffects = filter(one.getEffects(), true);
        boolean independent = !containsAny(join, negativeEffects);
        return independent && !containsAny(negativeEffects, join);
    }

    private static Set<Statement> filter(Set<Statement> statements, boolean isNegated) {
        Set<Statement> newSet = new HashSet<>();
        for (Statement statement : statements) {
            if(isNegated && statement.isNegated()){
                newSet.add(statement);
            }else if(!isNegated && !statement.isNegated()){
                newSet.add(statement);
            }
        }
        return newSet;
    }

    private static boolean containsAny(Set<Statement> dest, Set<Statement> source) {
        for (Statement st : source) {
            if (dest.contains(st)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkEmptyMutexPreconditions(Transition one, Transition other, Set<GraphPlan.Rel> stExclusion) {
        Set<Statement> onePreconds = one.stateTransition.getPreconditions();
        Set<Statement> otherPreconds = other.stateTransition.getPreconditions();
        for (Statement oneSt : onePreconds) {
            for (Statement otherSt : otherPreconds) {
                if (stExclusion.contains(new GraphPlan.Rel(oneSt, otherSt))) {
                    return false;
                }
            }
        }
        return true;
    }
}
