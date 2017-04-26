package com.drk.tools.gplannercore.planner.search.graphplan;

import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.search.SearchException;
import com.drk.tools.gplannercore.core.search.Searcher;
import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.core.state.Transition;
import com.drk.tools.gplannercore.planner.search.context.SearchContext;
import com.drk.tools.gplannercore.planner.search.unifier.OperatorUnifierBuilder;
import com.drk.tools.gplannercore.planner.search.unifier.SearchUnifier;
import com.drk.tools.gplannercore.planner.search.unifier.UnifierBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphPlan implements Searcher {

    private final Searcher searcher;

    public GraphPlan(Searcher searcher) {
        this.searcher = searcher;
    }

    @Override
    public void startSearch(SearchContext context, State initialState, State finalState) throws SearchException {
        Layer layer = firstLayer(initialState, context.getContext());
        boolean reachedGoal = Extraction.reachedGoal(layer, finalState);
        boolean twoConsecutiveEqualLayer = false;
        while (!reachedGoal && !twoConsecutiveEqualLayer) {
            Layer nextLayer = expandLayer(layer, context.getContext());
            reachedGoal = Extraction.reachedGoal(nextLayer, finalState);
            twoConsecutiveEqualLayer = layer.equals(nextLayer);
            layer = nextLayer;
        }

        if (reachedGoal) {
            List<Set<Transition>> layeredPlan = Extraction.extract(layer, finalState);
            Set<Transition> flatSet = new HashSet<>();
            for (Set<Transition> set : layeredPlan) {
                flatSet.addAll(set);
            }
            SearchContext.Builder builder = new SearchContext.Builder(context);
            builder.transitions(new ArrayList<>(flatSet));
            searcher.startSearch(builder.build(), initialState, finalState);
        } else {
            context.close();
        }
    }


    private Layer firstLayer(State initialState, Context context) {
        UnifierBuilder unifierBuilder = new OperatorUnifierBuilder(context);
        SearchUnifier searchUnifier = unifierBuilder.from(initialState.getStatements(), new HashSet<Statement>());
        Set<Rel> statementExclusions = new HashSet<>();
        Set<Statement> statements = new HashSet<>(initialState.getStatements());
        Set<Transition> transitions = new HashSet<>(searchUnifier.all());
        Set<Rel> transitionExclusions = Mutex.mutexTransitions(transitions, statementExclusions);
        return new Layer(null, statementExclusions, statements, transitions, transitionExclusions);
    }

    private Layer expandLayer(Layer layer, Context context) {
        Set<Statement> statements = new HashSet<>(layer.statements);
        statements.addAll(transitionEffects(layer.applicableTransitions));
        UnifierBuilder unifierBuilder = new OperatorUnifierBuilder(context);
        SearchUnifier searchUnifier = unifierBuilder.from(statements, new HashSet<Statement>());
        Set<Rel> statementExclusions = Mutex.mutexStatements(statements, layer);
        Set<Transition> transitions = new HashSet<>(searchUnifier.all());
        Set<Rel> transitionExclusions = Mutex.mutexTransitions(transitions, statementExclusions);
        return new Layer(layer, statementExclusions, statements, transitions, transitionExclusions);
    }

    private Set<Statement> transitionEffects(Set<Transition> transitions) {
        Set<Statement> statements = new HashSet<>();
        for (Transition transition : transitions) {
            statements.addAll(transition.stateTransition.getPositiveEffects());
        }
        return statements;
    }

    static class Layer {

        final Layer lastLayer;
        final Set<Rel> statementExclusions;
        final Set<Statement> statements;
        final Set<Transition> applicableTransitions;
        final Set<Rel> transitionExclusions;

        Layer(Layer lastLayer, Set<Rel> statementExclusions, Set<Statement> statements, Set<Transition> applicableTransitions, Set<Rel> transitionExclusions) {
            this.lastLayer = lastLayer;
            this.statementExclusions = statementExclusions;
            this.statements = statements;
            this.applicableTransitions = applicableTransitions;
            this.transitionExclusions = transitionExclusions;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Layer) {
                Layer otherLayer = (Layer) obj;
                return statements.equals(otherLayer.statements)
                        && applicableTransitions.equals(otherLayer.applicableTransitions)
                        && transitionExclusions.equals(otherLayer.transitionExclusions);
            }
            return super.equals(obj);
        }

    }

    static class Rel {
        private final Object one;
        private final Object other;

        Rel(Object one, Object other) {
            this.one = one;
            this.other = other;
        }

        @Override
        public int hashCode() {
            return one.hashCode() * other.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Rel) {
                Rel otherRel = (Rel) obj;
                return (one.equals(otherRel.one) && other.equals(otherRel.other))
                        || one.equals(otherRel.other) || other.equals(otherRel.one);
            }
            return false;
        }
    }


}
