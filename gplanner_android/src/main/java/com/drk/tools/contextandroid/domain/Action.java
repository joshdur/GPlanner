package com.drk.tools.contextandroid.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Action {

    public enum Type {
        CHANGE_SCREEN,
        INTENT,
        ADD_VIEWS
    }

    public final Type type;
    public final String screenName;
    public final IntentData intentData;
    public final Set<ViewInfo> viewInfos;

    private Action(Type type, String screenName, IntentData intentData, Set<ViewInfo> viewInfos) {
        this.type = type;
        this.screenName = screenName;
        this.intentData = intentData;
        this.viewInfos = viewInfos;
    }

    public static Action changeToScreen(String name) {
        return new Action(Type.CHANGE_SCREEN, name, null, null);
    }

    public static Action launchIntent(IntentData intentData) {
        return new Action(Type.INTENT, null, intentData, null);
    }

    public static Action addViews(ViewInfo... viewInfos){
        Set<ViewInfo> infos = new HashSet<>(Arrays.asList(viewInfos));
        return new Action(Type.ADD_VIEWS, null, null, infos);
    }

}
