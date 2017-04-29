package com.drk.tools.contextandroid.planner.domain;

import com.drk.tools.contextandroid.domain.ElementInputText;
import com.drk.tools.contextandroid.domain.ElementText;
import com.drk.tools.contextandroid.planner.variables.Element;

import java.util.HashMap;

public class TextInfo {

    private final HashMap<Element, ElementText> texts;
    private final HashMap<Element, ElementInputText> inputTexts;

    public TextInfo(HashMap<Element, ElementText> texts, HashMap<Element, ElementInputText> inputTexts) {
        this.texts = texts;
        this.inputTexts = inputTexts;
    }

    public boolean isTextDefined(Element element) {
        return texts.containsKey(element);
    }

    public boolean isInputTextDefined(Element element) {
        return inputTexts.containsKey(element);
    }

    public ElementText getText(Element element) {
        return texts.get(element);
    }

    public ElementInputText getInputText(Element element) {
        return inputTexts.get(element);
    }
}
