package com.drk.tools.contextandroid.domain;

public class ElementInputText {

    public final int resId;
    public final String text;
    public final boolean pressImeActionButton;

    ElementInputText(int resId, String text, boolean pressImeActionButton) {
        this.resId = resId;
        this.text = text;
        this.pressImeActionButton = pressImeActionButton;
    }
}
