package com.drk.tools.contextandroid.planner.domain;

import com.drk.tools.contextandroid.planner.variables.Screen;

public class InitInfo {

    private final Screen initScreen;

    public InitInfo(Screen initScreen) {
        this.initScreen = initScreen;
    }

    public Screen getFirstScreen() {
        return initScreen;
    }

    public boolean hasInitScreen() {
        return initScreen != null;
    }

}
