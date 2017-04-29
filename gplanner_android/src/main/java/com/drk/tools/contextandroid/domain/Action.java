package com.drk.tools.contextandroid.domain;

public class Action {

    public enum Type {
        CHANGE_SCREEN,
        INTENT
    }

    public final Type type;
    public final String screenName;
    public final IntentData intentData;

    private Action(Type type, String screenName, IntentData intentData) {
        this.type = type;
        this.screenName = screenName;
        this.intentData = intentData;
    }

    public static Action changeToScreen(String name) {
        return new Action(Type.CHANGE_SCREEN, name, null);
    }

    public static Action launchIntent(IntentData intentData) {
        return new Action(Type.INTENT, null, intentData);
    }

}
