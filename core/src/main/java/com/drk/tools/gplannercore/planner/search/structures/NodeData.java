package com.drk.tools.gplannercore.planner.search.structures;

import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.core.state.Transition;
import com.drk.tools.gplannercore.planner.search.unifier.SearchUnifier;

import java.util.Set;

public class NodeData {

    public final SearchUnifier searchUnifier;
    public final Transition transition;
    public final State state;
    public final int cost;
    public final int heuristic;

    private NodeData(SearchUnifier searchUnifier, Transition transition, State state, int cost, int heuristic) {
        this.searchUnifier = searchUnifier;
        this.transition = transition;
        this.state = state;
        this.cost = cost;
        this.heuristic = heuristic;
    }

    public static NodeData from(SearchUnifier unifier, Transition transition, State state){
        return new NodeData(unifier, transition, state, 0, 0);
    }

    public static NodeData from(SearchUnifier unifier, Transition transition,  State state, int c, int h){
        return new NodeData(unifier, transition, state, c, h);
    }
}
