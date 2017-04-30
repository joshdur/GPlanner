package com.drk.tools.contextandroid.domain;

public class ElementText {

    public final ViewInfo viewInfo;
    public final String text;
    public final boolean allViews;
    public final boolean justContains;

    ElementText(ViewInfo viewInfo, String text, boolean allViews, boolean justContains) {
        this.viewInfo = viewInfo;
        this.text = text;
        this.allViews = allViews;
        this.justContains = justContains;
    }
}
