package com.drk.tools.gplannercore.planner.search.hsp;

import com.drk.tools.gplannercore.core.Plan;
import com.drk.tools.gplannercore.core.search.SearchException;
import com.drk.tools.gplannercore.core.search.Searcher;
import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.state.Transition;
import com.drk.tools.gplannercore.planner.search.context.SearchContext;
import com.drk.tools.gplannercore.planner.search.context.SearchNode;
import com.drk.tools.gplannercore.planner.search.hsp.heuristic.Score;
import com.drk.tools.gplannercore.planner.search.unifier.SearchUnifier;

import java.util.TreeSet;

public class HSP implements Searcher {

    private final Score score;

    public HSP(Score score) {
        this.score = score;
    }

    private Node buildNode(SearchContext context, State state, Transition transition, Node lastNode) {
        SearchUnifier unifier = context.getUnifier(state);
        int cost = lastNode == null ? 0 : lastNode.cost + 1;
        int heuristicValue = score.resolve(state);
        return new Node(lastNode, state, transition, unifier, cost, heuristicValue);
    }

    @Override
    public void startSearch(SearchContext context, State initialState, State finalState) throws SearchException {
        PriorityQueue queue = new PriorityQueue();
        Node root = buildNode(context, initialState, null, null);
        queue.push(root);

        try {
            while (!queue.isEmpty() && !context.isCancelled()) {
                context.assertRemainingTime();
                Node node = queue.peek();
                Transition transition = node.next();
                if (transition != null) {
                    State newState = context.applyEffects(node.state, transition);
                    if (!context.existsInSequence(newState, node)) {
                        Node newNode = buildNode(context, newState, transition, node);
                        if (context.validate(newState, finalState)) {
                            Plan plan = context.recoverSequence(newNode);
                            context.pushPlan(plan);
                            context.close();
                        } else {
                            queue.push(newNode);
                        }
                    }
                } else {
                    queue.pop();
                }
            }
            context.close();
        } catch (SearchException e) {
            context.close();
            throw e;
        }
    }

    private static class PriorityQueue {
        private final TreeSet<Node> treeSet;

        PriorityQueue() {
            treeSet = new TreeSet<>(new Comparator());
        }

        Node peek() {
            return treeSet.first();
        }

        Node pop() {
            Node node = treeSet.first();
            treeSet.remove(node);
            return node;
        }

        boolean isEmpty() {
            return treeSet.isEmpty();
        }

        void push(Node node) {
            if (node.heuristicValue != Integer.MAX_VALUE) {
                treeSet.add(node);
            }
        }

        private class Comparator implements java.util.Comparator<Node> {

            @Override
            public int compare(Node o1, Node o2) {
                return getValue(o1) - getValue(o2);
            }

            private int getValue(Node node) {
                return (node.cost + node.heuristicValue) * 10000 + node.state.hashCode();
            }
        }
    }


    private static class Node implements SearchNode {

        final Node lastNode;
        final State state;
        final Transition transition;
        final SearchUnifier searchUnifier;
        final int cost, heuristicValue;

        Node(Node lastNode, State state, Transition transition, SearchUnifier searchUnifier, int cost, int h) {
            this.lastNode = lastNode;
            this.state = state;
            this.transition = transition;
            this.searchUnifier = searchUnifier;
            this.cost = cost;
            this.heuristicValue = h;
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
