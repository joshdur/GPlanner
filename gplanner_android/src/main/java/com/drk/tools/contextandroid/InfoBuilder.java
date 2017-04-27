package com.drk.tools.contextandroid;

import com.drk.tools.contextandroid.domain.*;
import com.drk.tools.contextandroid.planner.domain.*;
import com.drk.tools.contextandroid.planner.variables.Bool;
import com.drk.tools.contextandroid.planner.variables.Element;
import com.drk.tools.contextandroid.planner.variables.PagerElement;
import com.drk.tools.contextandroid.planner.variables.Screen;
import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.planner.state.GStatement;

import java.util.*;

import static com.drk.tools.contextandroid.planner.atoms.MainAtoms.navigateTo;
import static com.drk.tools.contextandroid.planner.atoms.MainAtoms.screenNavigationPending;

class InfoBuilder {

    static TextInfo getTextInfo(AndroidViewInfo androidViewInfo, Scenario scenario) {
        HashMap<Element, String> textsToCheck = getHashElementString(androidViewInfo, scenario.textToCheck);
        HashMap<Element, String> textsToInput = getHashElementString(androidViewInfo, scenario.textToInput);
        return new TextInfo(textsToCheck, textsToInput);
    }

    private static HashMap<Element, String> getHashElementString(AndroidViewInfo androidViewInfo, Collection<ElementText> elementTexts) {
        HashMap<Element, String> hashElementString = new LinkedHashMap<>();
        for (ElementText elementText : elementTexts) {
            Element element = androidViewInfo.findElementWithId(elementText.resId);
            hashElementString.put(element, elementText.text);
        }
        return hashElementString;
    }


    static HierarchyInfo getHierarchyInfo(AndroidViewInfo androidViewInfo) {
        HashMap<ViewInfo, Element> inverseMapElements = inverse(androidViewInfo.mapElements);
        HashMap<PagerInfo, PagerElement> inverseMapPagers = inverse(androidViewInfo.mapPagers);

        HashMap<Element, HierarchyInfo.Parent> hashParents = new LinkedHashMap<>();
        HashMap<PagerElement, HierarchyInfo.Parent> hashPagerParents = new LinkedHashMap<>();
        for (Map.Entry<Screen, ScreenInfo> entry : androidViewInfo.mapScreens.entrySet()) {
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

    static BackInfo getBackInfo(AndroidViewInfo androidViewInfo) {

        HashMap<Screen, Screen> backData = new LinkedHashMap<>();
        for (Map.Entry<Screen, ScreenInfo> entry : androidViewInfo.mapScreens.entrySet()) {
            ScreenInfo screenInfo = entry.getValue();
            if (screenInfo.hashBackInfo()) {
                String screenName = screenInfo.back.screenName;
                backData.put(entry.getKey(), androidViewInfo.findScreenByName(screenName));
            }
        }
        return new BackInfo(backData);
    }

    private static ActionInfo.ActionData solveActionData(NavigationInfo navigationInfo, AndroidViewInfo androidViewInfo) {
        String screenName = navigationInfo.screenName;
        return navigationTo(screenName, androidViewInfo);
    }

    private static ActionInfo.ActionData navigationTo(String screenName, AndroidViewInfo androidViewInfo) {
        Screen screen = androidViewInfo.findScreenByName(screenName);
        Set<Statement> preconds = new HashSet<>();
        Set<Statement> positiveEffects = new HashSet<>();
        Set<Statement> negativeEffects = new HashSet<>();
        positiveEffects.add(GStatement.from(navigateTo, screen));
        positiveEffects.add(GStatement.from(screenNavigationPending, Bool.TRUE));
        return new ActionInfo.ActionData(preconds, positiveEffects, negativeEffects);
    }


    static ActionInfo getActionInfo(AndroidViewInfo androidViewInfo) {

        HashMap<Element, ActionInfo.ActionData> hashData = new LinkedHashMap<>();
        for (Map.Entry<Element, ViewInfo> entry : androidViewInfo.mapElements.entrySet()) {
            Element element = entry.getKey();
            ViewInfo viewInfo = entry.getValue();
            if (viewInfo.hasClickDefined()) {
                String screenName = viewInfo.navigationInfo.screenName;
                ActionInfo.ActionData actionData = solveActionData(viewInfo.navigationInfo, androidViewInfo);
                hashData.put(element, actionData);
            }
        }
        return new ActionInfo(hashData);
    }

    static InitInfo getInitInfo(AndroidViewInfo androidViewInfo) {
        List<Screen> screens = new ArrayList<>(androidViewInfo.mapScreens.keySet());
        if (!screens.isEmpty()) {
            Screen screen = screens.get(0);
            ScreenInfo screenInfo = androidViewInfo.mapScreens.get(screen);
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
