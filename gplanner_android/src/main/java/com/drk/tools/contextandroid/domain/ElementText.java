package com.drk.tools.contextandroid.domain;

public class ElementText {

    public final int resId;
    public final String text;
    public final boolean allViews;
    public final boolean justContains;

    ElementText(int resId, String text, boolean allViews, boolean justContains) {
        this.resId = resId;
        this.text = text;
        this.allViews = allViews;
        this.justContains = justContains;
    }
}
