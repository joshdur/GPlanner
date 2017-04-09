package com.drk.tools.gplannercore.planner.search.structures;

import java.util.Comparator;
import java.util.TreeSet;

public class PriorityQueueDataWrapper implements DataWrapper {

    private final TreeSet<Node> treeSet;

    PriorityQueueDataWrapper(){
        treeSet = new TreeSet<>(new TreeSetComparator());
    }

    @Override
    public Node first() {
        return treeSet.first();
    }

    @Override
    public Node pop() {
        Node node = treeSet.first();
        treeSet.remove(node);
        return node;
    }

    @Override
    public void push(Node node) {
        if(node.nodeData.heuristic != Integer.MAX_VALUE) {
            treeSet.add(node);
        }
    }

    @Override
    public boolean isEmpty() {
        return treeSet.isEmpty();
    }

    private class TreeSetComparator implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            return getValue(o1) - getValue(o2);
        }

        private int getValue(Node node) {
            NodeData nodeData = node.nodeData;
            return (nodeData.cost + nodeData.heuristic) * 10000 + nodeData.state.hashCode();
        }
    }
}
