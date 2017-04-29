package com.drk.tools.contextandroid.planner.domain;

import com.drk.tools.contextandroid.domain.ElementState;
import com.drk.tools.contextandroid.planner.variables.Element;

import java.util.HashMap;

public class ElementStateInfo {

    private final HashMap<Element, ElementState> hashState;

    public ElementStateInfo(HashMap<Element, ElementState> hashState) {
        this.hashState = hashState;
    }

    public ElementState getElementState(Element element){
        return hashState.get(element);
    }
}
