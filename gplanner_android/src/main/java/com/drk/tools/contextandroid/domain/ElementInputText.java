package com.drk.tools.contextandroid.domain;

public class ElementInputText {

    public final ViewInfo viewInfo;
    public final String text;
    public final boolean pressImeActionButton;

    ElementInputText(ViewInfo viewInfo, String text, boolean pressImeActionButton) {
        this.viewInfo = viewInfo;
        this.text = text;
        this.pressImeActionButton = pressImeActionButton;
    }
}
