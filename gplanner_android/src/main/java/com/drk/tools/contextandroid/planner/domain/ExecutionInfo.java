package com.drk.tools.contextandroid.planner.domain;

import java.util.ArrayList;
import java.util.List;

public class ExecutionInfo {

    public final List<Action> actions = new ArrayList<>();

    public void add(String name, Runnable runnable) {
        actions.add(new Action(name, runnable));
    }

    public void clear() {
        actions.clear();
    }

    public static class Action {

        public final String name;
        public final Runnable runnable;

        Action(String name, Runnable runnable) {
            this.name = name;
            this.runnable = runnable;
        }
    }
}
