package com.drk.tools.gplannercore.planner.search.forward;

import com.drk.tools.gplannercore.core.Plan;
import com.drk.tools.gplannercore.core.search.SearchException;
import com.drk.tools.gplannercore.core.search.Searcher;
import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.state.Transition;
import com.drk.tools.gplannercore.core.search.context.SearchContext;
import com.drk.tools.gplannercore.core.search.context.SearchNode;
import com.drk.tools.gplannercore.planner.search.unifier.SearchUnifier;

import java.util.Stack;

public class SimpleForward implements Searcher {

    private final boolean debug;

    private Node buildNode(State state, Transition transition, Node lastNode, SearchUnifier unifier) {
        return new Node(lastNode, state, transition, unifier);
    }

    public SimpleForward(boolean debug) {
        this.debug = debug;
    }

    public SimpleForward() {
        this(false);
    }

    @Override
    public void startSearch(SearchContext searchContext, State initialState, State finalState) throws SearchException {
        Stack<Node> stack = new Stack<>();
        Node root = buildNode(initialState, null, null, searchContext.getUnifier(initialState));
        stack.push(root);
        try {
            while (!stack.isEmpty() && !searchContext.isCancelled()) {
                searchContext.assertRemainingTime();
                Node node = stack.peek();
                Transition transition = node.next();
                if (transition != null) {
                    State newState = searchContext.applyEffects(node.state, transition);
                    if (!searchContext.existsInSequence(newState, node)) {
                        Node newNode = buildNode(newState, transition, node, searchContext.getUnifier(newState));
                        if (debug) {
                            Plan plan = searchContext.recoverSequence(newNode);
                            System.out.println();
                            System.out.println(searchContext.getContext().asString(plan));
                            System.out.println(newState.toString());
                        }

                        if (searchContext.validate(newState, finalState)) {
                            Plan plan = searchContext.recoverSequence(newNode);
                            searchContext.pushPlan(plan);
                        } else {
                            stack.push(newNode);
                        }
                    }
                } else {

                    stack.pop();
                }
            }
            searchContext.close();
        } catch (SearchException e) {
            searchContext.close();
            throw e;
        }
    }

    private static class Node implements SearchNode {

        final Node lastNode;
        final State state;
        final Transition transition;
        final SearchUnifier searchUnifier;

        Node(Node lastNode, State state, Transition transition, SearchUnifier searchUnifier) {
            this.lastNode = lastNode;
            this.state = state;
            this.transition = transition;
            this.searchUnifier = searchUnifier;
        }

        Transition next() {
            return searchUnifier.next();
        }

        @Override
        public State getState() {
            return state;
        }

        @Override
        public SearchNode getLastNode() {
            return lastNode;
        }

        @Override
        public Transition getTransition() {
            return transition;
        }
    }
}
