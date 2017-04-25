package com.drk.tools.contextandroid.domain;

public class NavigationInfo {

    public enum Type {
        CHANGE_SCREEN
    }

    public final Type type;
    public final String screenName;

    private NavigationInfo(Type type, String screenName) {
        this.type = type;
        this.screenName = screenName;
    }

    public static NavigationInfo changeToScreen(String name) {
        return new NavigationInfo(Type.CHANGE_SCREEN, name);
    }
}
