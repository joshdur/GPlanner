package com.drk.tools.contextandroid.planner.domain;

import com.drk.tools.contextandroid.planner.variables.Screen;

public class InitInfo {

    private final Screen initScreen;
    private final String screenName;

    public InitInfo(Screen initScreen, String screenName) {
        this.initScreen = initScreen;
        this.screenName = screenName;
    }

    public Screen getFirstScreen() {
        return initScreen;
    }

    public boolean hasInitScreen() {
        return initScreen != null;
    }

}
