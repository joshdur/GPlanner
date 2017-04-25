package com.drk.tools.contextandroid.planner.domain;

import com.drk.tools.contextandroid.planner.variables.Screen;

import java.util.HashMap;

public class BackInfo {

    private final HashMap<Screen, Screen> hashBackData;

    public BackInfo(HashMap<Screen, Screen> hashBackData) {
        this.hashBackData = hashBackData;
    }

    public boolean isBackInfoDefined(Screen screen) {
        return hashBackData.containsKey(screen);
    }

    public Screen backOf(Screen screen) {
        return hashBackData.get(screen);
    }
}
