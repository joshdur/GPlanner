package com.drk.tools.contextandroid;

import com.drk.tools.contextandroid.domain.*;
import com.drk.tools.contextandroid.planner.domain.*;
import com.drk.tools.contextandroid.planner.variables.Element;
import com.drk.tools.contextandroid.planner.variables.PagerElement;
import com.drk.tools.contextandroid.planner.variables.Screen;

import java.util.*;

class InfoBuilder {

    private final AndroidViewInfo info;
    private final boolean debug;

    InfoBuilder(AndroidViewInfo info, boolean debug) {
        this.info = info;
        this.debug = debug;
    }

    TextInfo getTextInfo(Scenario scenario) {
        return new TextInfo(scenario.textToCheck, scenario.textToInput, info);
    }

    ElementStateInfo getElementStateInfo(Scenario scenario) {
        HashMap<Element, ElementState> hashElementState = new LinkedHashMap<>();
        for (ElementState elementState : scenario.elementStates) {
            Element element = info.findElementWithViewInfo(elementState.info);
            hashElementState.put(element, elementState);
        }
        return new ElementStateInfo(hashElementState);
    }

    HierarchyInfo getHierarchyInfo() {
        HashMap<ViewInfo, Element> inverseMapElements = inverse(info.mapElements);
        HashMap<PagerInfo, PagerElement> inverseMapPagers = inverse(info.mapPagers);

        HashMap<Element, HierarchyInfo.Parent> hashParents = new LinkedHashMap<>();
        HashMap<PagerElement, HierarchyInfo.Parent> hashPagerParents = new LinkedHashMap<>();
        for (Map.Entry<Screen, ScreenInfo> entry : info.mapScreens.entrySet()) {
            Screen screen = entry.getKey();
            ScreenInfo screenInfo = entry.getValue();
            for (ViewInfo viewInfo : screenInfo.views) {
                Element element = inverseMapElements.get(viewInfo);
                hashParents.put(element, new HierarchyInfo.Parent(screen, null, -1));
            }
            for (PagerInfo pagerInfo : screenInfo.pagers) {
                PagerElement pagerElement = inverseMapPagers.get(pagerInfo);
                hashPagerParents.put(pagerElement, new HierarchyInfo.Parent(screen, null, -1));
                for (Map.Entry<Integer, List<ViewInfo>> e : pagerInfo.views.entrySet()) {
                    int page = e.getKey();
                    for (ViewInfo viewInfo : e.getValue()) {
                        Element element = inverseMapElements.get(viewInfo);
                        hashParents.put(element, new HierarchyInfo.Parent(null, pagerElement, page));
                    }
                }
            }
        }
        return new HierarchyInfo(hashParents, hashPagerParents);
    }

    BackInfo getBackInfo() {
        HashMap<Screen, Screen> backData = new LinkedHashMap<>();
        for (Map.Entry<Screen, ScreenInfo> entry : info.mapScreens.entrySet()) {
            ScreenInfo screenInfo = entry.getValue();
            if (screenInfo.hashBackInfo()) {
                String screenName = screenInfo.back.screenName;
                backData.put(entry.getKey(), info.findScreenByName(screenName));
            }
        }
        return new BackInfo(backData);
    }


    ActionInfo getActionInfo() {
        HashMap<Element, Action> hashData = new LinkedHashMap<>();
        HashMap<Element, Action> hashImeData = new LinkedHashMap<>();
        for (Map.Entry<Element, ViewInfo> entry : info.mapElements.entrySet()) {
            Element element = entry.getKey();
            ViewInfo viewInfo = entry.getValue();
            if (viewInfo.hasClickDefined()) {
                hashData.put(element, viewInfo.clickAction);
            }
            if(viewInfo.hasImeOptionsClickDefined()){
                hashImeData.put(element, viewInfo.imeOptionsClickAction);
            }
        }
        return new ActionInfo(hashData, hashImeData, info, debug);
    }

    InitInfo getInitInfo() {
        List<Screen> screens = new ArrayList<>(info.mapScreens.keySet());
        if (!screens.isEmpty()) {
            Screen screen = screens.get(0);
            ScreenInfo screenInfo = info.mapScreens.get(screen);
            String screenName = screenInfo.name;
            return new InitInfo(screen, screenName);
        }
        return new InitInfo(null, null);
    }

    private static <A, B> HashMap<B, A> inverse(HashMap<A, B> hash) {
        HashMap<B, A> inverse = new LinkedHashMap<>();
        for (A key : hash.keySet()) {
            inverse.put(hash.get(key), key);
        }
        return inverse;
    }


}
