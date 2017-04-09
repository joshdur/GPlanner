package com.drk.tools.gplannercore.planner.search.structures;

import java.util.Stack;

public class StackDataWrapper implements DataWrapper {

    private final Stack<Node> stack = new Stack<>();

    @Override
    public Node first() {
        return stack.peek();
    }

    @Override
    public Node pop() {
        return stack.pop();
    }

    @Override
    public void push(Node node) {
        stack.push(node);
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }
}
