package com.drk.tools.gplannercore.planner.search;

import com.drk.tools.gplannercore.core.Plan;
import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.state.Transition;
import com.drk.tools.gplannercore.planner.search.structures.Node;
import com.drk.tools.gplannercore.planner.search.structures.NodeData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class NodeUtils {

    static boolean existsInSequence(State newState, Node node) {
        Node currentNode = node;
        while (currentNode != null) {
            NodeData nodeData = node.nodeData;
            if (nodeData.state.equals(newState)) {
                return true;
            } else {
                currentNode = currentNode.lastNode;
            }
        }
        return false;
    }

    static Plan recoverSequence(Node newNode) {
        List<Transition> transitions = new ArrayList<>();
        Node node = newNode;
        while (node != null) {
            NodeData nodeData = node.nodeData;
            if (nodeData.transition != null) {
                transitions.add(nodeData.transition);
            }
            node = node.lastNode;
        }
        Collections.reverse(transitions);
        return new Plan(transitions);
    }
}
