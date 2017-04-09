package com.drk.tools.gplannercore.core;

import com.drk.tools.gplannercore.core.state.Transition;

import java.util.List;

public class Plan {

    public final List<Transition> transitions;

    public Plan(List<Transition> transitions) {
        this.transitions = transitions;
    }
}
