package com.drk.tools.gplannercore.planner.search.structures;

public class Node {

    public final Node lastNode;
    public final NodeData nodeData;

    Node(Node lastNode, NodeData nodeData) {
        this.lastNode = lastNode;
        this.nodeData = nodeData;
    }
}
