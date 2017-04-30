package com.drk.tools.contextandroid.planner.domain;

import com.drk.tools.contextandroid.domain.AndroidViewInfo;
import com.drk.tools.contextandroid.domain.ElementInputText;
import com.drk.tools.contextandroid.domain.ElementText;
import com.drk.tools.contextandroid.domain.ViewInfo;
import com.drk.tools.contextandroid.planner.variables.Element;

import java.util.List;
import java.util.Set;

public class TextInfo {

    private final Set<ElementText> elementTexts;
    private final Set<ElementInputText> elementInputTexts;
    private final AndroidViewInfo info;

    public TextInfo(Set<ElementText> elementTexts, Set<ElementInputText> elementInputTexts, AndroidViewInfo info) {
        this.elementTexts = elementTexts;
        this.elementInputTexts = elementInputTexts;
        this.info = info;
    }

    public boolean isTextDefined(Element element) {
        ViewInfo viewInfo = info.mapElements.get(element);
        return viewInfo != null && getDefinedTextElement(viewInfo) != null;
    }

    public boolean isInputTextDefined(Element element) {
        ViewInfo viewInfo = info.mapElements.get(element);
        return viewInfo != null && getDefinedTextInputElement(viewInfo) != null;
    }

    public ElementText getText(Element element) {
        ViewInfo viewInfo = info.mapElements.get(element);
        return getDefinedTextElement(viewInfo);
    }

    public ElementInputText getInputText(Element element) {
        ViewInfo viewInfo = info.mapElements.get(element);
        return getDefinedTextInputElement(viewInfo);
    }

    private ElementText getDefinedTextElement(ViewInfo viewInfo) {
        for (ElementText elementText : elementTexts) {
            if (elementText.viewInfo.matches(viewInfo)) {
                return elementText;
            }
        }
        return null;
    }

    private ElementInputText getDefinedTextInputElement(ViewInfo viewInfo) {
        for (ElementInputText elementInputText : elementInputTexts) {
            if (elementInputText.viewInfo.matches(viewInfo)) {
                return elementInputText;
            }
        }
        return null;
    }

}
