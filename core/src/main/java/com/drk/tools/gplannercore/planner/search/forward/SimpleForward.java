package com.drk.tools.gplannercore.planner.search.forward;

import com.drk.tools.gplannercore.core.Plan;
import com.drk.tools.gplannercore.core.search.SearchException;
import com.drk.tools.gplannercore.core.search.Searcher;
import com.drk.tools.gplannercore.core.search.context.SearchContext;
import com.drk.tools.gplannercore.core.search.context.SearchNode;
import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.state.Transition;
import com.drk.tools.gplannercore.planner.logger.EmptyLogger;
import com.drk.tools.gplannercore.planner.logger.LogLevel;
import com.drk.tools.gplannercore.planner.logger.Logger;
import com.drk.tools.gplannercore.planner.search.unifier.SearchUnifier;

import java.util.Stack;

public class SimpleForward implements Searcher {

    private final LogLevel logLevel;
    private final Logger logger;

    private Node buildNode(State state, Transition transition, Node lastNode, SearchUnifier unifier) {
        return new Node(lastNode, state, transition, unifier);
    }

    public SimpleForward(LogLevel logLevel, Logger logger) {
        this.logLevel = logLevel;
        this.logger = logger;
    }

    public SimpleForward() {
        this(LogLevel.NONE, new EmptyLogger());
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
                        if (logLevel.isOver(LogLevel.DEBUG)) {
                            logger.log("SimpleForward", searchContext.getContext().asString(transition));
                            logger.log("SimpleForward", newState.toString());
                            logger.log("SimpleForward", "");
                        }
                        if (logLevel == LogLevel.VERBOSE) {
                            logger.log("SimpleForward", searchContext.getContext().asString(searchContext.recoverSequence(newNode)));
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
            if(logLevel.isOver(LogLevel.ERROR)){
                logger.log("SimpleForward", "Error in SimpleForward", e);
            }
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
