package com.drk.tools.gplannercore.planner.search.structures;

public interface DataWrapper {

    Node first();

    Node pop();

    void push(Node node);

    boolean isEmpty();
}
