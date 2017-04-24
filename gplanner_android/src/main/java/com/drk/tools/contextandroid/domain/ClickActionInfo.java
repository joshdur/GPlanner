package com.drk.tools.contextandroid.domain;

public class ClickActionInfo {

    private final String screenName;

    private ClickActionInfo(String screenName) {
        this.screenName = screenName;
    }

    public static ClickActionInfo changeToScreen(String name) {
        return new ClickActionInfo(name);
    }
}
