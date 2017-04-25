package com.drk.tools.contextandroid;

import com.drk.tools.contextandroid.domain.PagerInfo;
import com.drk.tools.contextandroid.domain.ScreenInfo;
import com.drk.tools.contextandroid.domain.ViewInfo;
import com.drk.tools.contextandroid.planner.domain.*;
import com.drk.tools.contextandroid.planner.variables.*;

import java.util.*;

import static com.drk.tools.contextandroid.planner.domain.HierarchyInfo.Parent;

public class AndroidViewInfo {

    private final HashMap<Screen, ScreenInfo> mapScreens;
    private final HashMap<Element, ViewInfo> mapElements;
    private final HashMap<PagerElement, PagerInfo> mapPagers;
    private final HashMap<Injection, Enum> mapInjections;
    private final HashMap<Request, Enum> mapRequests;

    public static Builder builder() {
        return new Builder();
    }

    private AndroidViewInfo(Builder builder) {
        mapScreens = builder.mapScreens;
        mapElements = builder.mapElements;
        mapPagers = builder.mapPagers;
        mapInjections = builder.mapInjections;
        mapRequests = builder.mapRequests;
    }

    HierarchyInfo getHierarchyInfo() {
        HashMap<ViewInfo, Element> inverseMapElements = inverse(mapElements);
        HashMap<PagerInfo, PagerElement> inverseMapPagers = inverse(mapPagers);

        HashMap<Element, HierarchyInfo.Parent> hashParents = new LinkedHashMap<>();
        HashMap<PagerElement, HierarchyInfo.Parent> hashPagerParents = new LinkedHashMap<>();
        for (Map.Entry<Screen, ScreenInfo> entry : mapScreens.entrySet()) {
            Screen screen = entry.getKey();
            ScreenInfo screenInfo = entry.getValue();
            for (ViewInfo viewInfo : screenInfo.views) {
                Element element = inverseMapElements.get(viewInfo);
                hashParents.put(element, new Parent(screen, null, -1));
            }
            for (PagerInfo pagerInfo : screenInfo.pagers) {
                PagerElement pagerElement = inverseMapPagers.get(pagerInfo);
                hashPagerParents.put(pagerElement, new Parent(screen, null, -1));
                for (Map.Entry<Integer, List<ViewInfo>> e : pagerInfo.views.entrySet()) {
                    int page = e.getKey();
                    for (ViewInfo viewInfo : e.getValue()) {
                        Element element = inverseMapElements.get(viewInfo);
                        hashParents.put(element, new Parent(null, pagerElement, page));
                    }
                }
            }
        }
        return new HierarchyInfo(hashParents, hashPagerParents);
    }

    TextInfo getTextInfo(Set<IdText> checkTexts, Set<IdText> inputTexts) {
        return null;
    }

    BackInfo getBackInfo() {
        ActionSolver actionSolver = new ActionSolver(mapScreens);
        HashMap<Screen, Screen> backData = new LinkedHashMap<>();
        for (Map.Entry<Screen, ScreenInfo> entry : mapScreens.entrySet()) {
            ScreenInfo screenInfo = entry.getValue();
            backData.put(entry.getKey(), actionSolver.solveBackData(screenInfo.back));
        }
        return new BackInfo(backData);
    }

    ActionInfo getActionInfo() {
        ActionSolver actionSolver = new ActionSolver(mapScreens);
        HashMap<Element, ActionInfo.ActionData> hashData = new LinkedHashMap<>();
        for (Map.Entry<Element, ViewInfo> entry : mapElements.entrySet()) {
            Element element = entry.getKey();
            ViewInfo viewInfo = entry.getValue();
            if (viewInfo.hasClickDefined()) {
                ActionInfo.ActionData actionData = actionSolver.solveActionData(viewInfo.navigationInfo);
                hashData.put(element, actionData);
            }
        }
        return new ActionInfo(hashData);
    }

    InitInfo getInitInfo() {
        List<Screen> screens = new ArrayList<>(mapScreens.keySet());
        return new InitInfo(!screens.isEmpty() ? screens.get(0) : null);
    }

    private <A, B> HashMap<B, A> inverse(HashMap<A, B> hash) {
        HashMap<B, A> inverse = new LinkedHashMap<>();
        for (A key : hash.keySet()) {
            inverse.put(hash.get(key), key);
        }
        return inverse;
    }

    public static class Builder {

        private HashMap<Screen, ScreenInfo> mapScreens;
        private HashMap<Element, ViewInfo> mapElements;
        private HashMap<PagerElement, PagerInfo> mapPagers;
        private HashMap<Injection, Enum> mapInjections;
        private HashMap<Request, Enum> mapRequests;

        private Builder() {
            mapScreens = new LinkedHashMap<>();
            mapElements = new LinkedHashMap<>();
            mapPagers = new LinkedHashMap<>();
            mapInjections = new LinkedHashMap<>();
            mapRequests = new LinkedHashMap<>();
        }

        public Builder addScreen(ScreenInfo screenInfo) {
            mapScreens.put(Screen.values()[mapScreens.size()], screenInfo);
            addViewInfos(screenInfo.views);
            for (PagerInfo pagerInfo : screenInfo.pagers) {
                mapPagers.put(PagerElement.values()[mapPagers.size()], pagerInfo);
                for (Map.Entry<Integer, List<ViewInfo>> entry : pagerInfo.views.entrySet()) {
                    addViewInfos(entry.getValue());
                }
            }
            return this;
        }

        private void addViewInfos(Collection<ViewInfo> viewInfos) {
            for (ViewInfo info : viewInfos) {
                mapElements.put(Element.values()[mapElements.size()], info);
            }
        }

        public Builder addInjections(Enum[] injections) {
            for (Enum e : injections) {
                mapInjections.put(Injection.values()[mapInjections.size()], e);
            }
            return this;
        }

        public Builder addRequests(Enum[] requests) {
            for (Enum e : requests) {
                mapRequests.put(Request.values()[mapRequests.size()], e);
            }
            return this;
        }

        public AndroidViewInfo build() {
            return new AndroidViewInfo(this);
        }

    }
}
