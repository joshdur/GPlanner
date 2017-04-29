package com.drk.tools.contextandroid;

import com.drk.tools.contextandroid.domain.*;
import com.drk.tools.contextandroid.planner.domain.*;
import com.drk.tools.contextandroid.planner.variables.*;
import com.drk.tools.gplannercore.core.Atom;
import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.planner.state.GStatement;
import com.drk.tools.gplannercore.planner.state.debug.DebugStatement;

import java.util.*;

import static com.drk.tools.contextandroid.planner.atoms.MainAtoms.*;

class InfoBuilder {

    private final AndroidViewInfo info;
    private final boolean debug;

    InfoBuilder(AndroidViewInfo info, boolean debug) {
        this.info = info;
        this.debug = debug;
    }

    TextInfo getTextInfo(Scenario scenario) {
        HashMap<Element, String> textsToCheck = getHashElementString(scenario.textToCheck);
        HashMap<Element, String> textsToInput = getHashElementString(scenario.textToInput);
        return new TextInfo(textsToCheck, textsToInput);
    }

    private HashMap<Element, String> getHashElementString(Collection<ElementText> elementTexts) {
        HashMap<Element, String> hashElementString = new LinkedHashMap<>();
        for (ElementText elementText : elementTexts) {
            Element element = info.findElementWithId(elementText.resId);
            hashElementString.put(element, elementText.text);
        }
        return hashElementString;
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

    private ActionInfo.ActionData solveActionData(Action action) {
        if (action.type == Action.Type.CHANGE_SCREEN) {
            return navigationTo(action.screenName);
        } else if (action.type == Action.Type.INTENT) {
            return intentTo(action.intentData);
        }
        return null;
    }

    private ActionInfo.ActionData navigationTo(String screenName) {
        Screen screen = info.findScreenByName(screenName);
        Set<Statement> preconds = new HashSet<>();
        Set<Statement> positiveEffects = new HashSet<>();
        Set<Statement> negativeEffects = new HashSet<>();
        positiveEffects.add(buildStatement(navigateTo, screen));
        positiveEffects.add(buildStatement(screenNavigationPending, Bool.TRUE));
        negativeEffects.add(buildStatement(screenNavigationPending, Bool.FALSE));
        return new ActionInfo.ActionData(preconds, positiveEffects, negativeEffects);
    }

    private ActionInfo.ActionData intentTo(IntentData intentData) {
        Intent intent = info.findIntentByName(intentData);
        Set<Statement> preconds = new HashSet<>();
        Set<Statement> positiveEffects = new HashSet<>();
        Set<Statement> negativeEffects = new HashSet<>();
        positiveEffects.add(buildStatement(intentTo, intent));
        positiveEffects.add(buildStatement(launchIntentPending, Bool.TRUE));
        negativeEffects.add(buildStatement(launchIntentPending, Bool.FALSE));
        return new ActionInfo.ActionData(preconds, positiveEffects, negativeEffects);
    }

    ActionInfo getActionInfo() {
        HashMap<Element, ActionInfo.ActionData> hashData = new LinkedHashMap<>();
        for (Map.Entry<Element, ViewInfo> entry : info.mapElements.entrySet()) {
            Element element = entry.getKey();
            ViewInfo viewInfo = entry.getValue();
            if (viewInfo.hasClickDefined()) {
                ActionInfo.ActionData actionData = solveActionData(viewInfo.action);
                if (actionData != null) {
                    hashData.put(element, actionData);
                }
            }
        }
        return new ActionInfo(hashData);
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

    private <A extends Atom<E>, E extends Enum> Statement buildStatement(A atom, E variable) {
        if (debug) {
            return DebugStatement.from(atom, variable);
        }
        return GStatement.from(atom, variable);
    }

    private static <A, B> HashMap<B, A> inverse(HashMap<A, B> hash) {
        HashMap<B, A> inverse = new LinkedHashMap<>();
        for (A key : hash.keySet()) {
            inverse.put(hash.get(key), key);
        }
        return inverse;
    }
}
