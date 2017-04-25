package com.drk.tools.contextandroid.planner.domain;

import com.drk.tools.contextandroid.planner.variables.Element;

import java.util.HashMap;

public class TextInfo {

    private final HashMap<Element, String> texts;
    private final HashMap<Element, String> inputTexts;

    public TextInfo(HashMap<Element, String> texts, HashMap<Element, String> inputTexts) {
        this.texts = texts;
        this.inputTexts = inputTexts;
    }

    public boolean isTextDefined(Element element) {
        return texts.containsKey(element);
    }

    public boolean isInputTextDefined(Element element) {
        return inputTexts.containsKey(element);
    }
}
