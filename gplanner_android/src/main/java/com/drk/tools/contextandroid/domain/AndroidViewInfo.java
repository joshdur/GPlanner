package com.drk.tools.contextandroid.domain;

import com.drk.tools.contextandroid.planner.variables.Element;
import com.drk.tools.contextandroid.planner.variables.Mock;
import com.drk.tools.contextandroid.planner.variables.PagerElement;
import com.drk.tools.contextandroid.planner.variables.Screen;

import java.util.*;

public class AndroidViewInfo {

    public final HashMap<Screen, ScreenInfo> mapScreens;
    public final HashMap<Element, ViewInfo> mapElements;
    public final HashMap<PagerElement, PagerInfo> mapPagers;
    public final HashMap<Mock, Enum> mapMocks;

    public static Builder builder() {
        return new Builder();
    }

    private AndroidViewInfo(Builder builder) {
        mapScreens = builder.mapScreens;
        mapElements = builder.mapElements;
        mapPagers = builder.mapPagers;
        mapMocks = builder.mapMocks;

    }

    public Element findElementWithId(int resId) {
        for (Map.Entry<Element, ViewInfo> entry : mapElements.entrySet()) {
            ViewInfo viewInfo = entry.getValue();
            if (viewInfo.id == resId) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException("Element with " + resId + " not found while building TextInfo");
    }

    public Screen findScreenByName(String name) {
        for (Map.Entry<Screen, ScreenInfo> entry : mapScreens.entrySet()) {
            if (entry.getValue().name.equalsIgnoreCase(name)) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException("Not found screenInfo for " + name);
    }

    public Mock findMockByEnum(Enum mock) {
        for (Map.Entry<Mock, Enum> entry : mapMocks.entrySet()) {
            if (entry.getValue().equals(mock)) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException("Not found mockInfo for " + mock.name());
    }


    public static class Builder {

        private HashMap<Screen, ScreenInfo> mapScreens;
        private HashMap<Element, ViewInfo> mapElements;
        private HashMap<PagerElement, PagerInfo> mapPagers;
        private HashMap<Mock, Enum> mapMocks;

        private Builder() {
            mapScreens = new LinkedHashMap<>();
            mapElements = new LinkedHashMap<>();
            mapPagers = new LinkedHashMap<>();
            mapMocks = new LinkedHashMap<>();
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

        public Builder addMocks(Enum[] injections) {
            for (Enum e : injections) {
                mapMocks.put(Mock.values()[mapMocks.size()], e);
            }
            return this;
        }

        public AndroidViewInfo build() {
            return new AndroidViewInfo(this);
        }

    }
}
