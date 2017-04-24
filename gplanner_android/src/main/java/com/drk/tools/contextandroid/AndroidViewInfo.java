package com.drk.tools.contextandroid;

import com.drk.tools.contextandroid.domain.PagerInfo;
import com.drk.tools.contextandroid.domain.ScreenInfo;
import com.drk.tools.contextandroid.domain.ViewInfo;
import com.drk.tools.contextandroid.planner.domain.ActionInfo;
import com.drk.tools.contextandroid.planner.domain.BackInfo;
import com.drk.tools.contextandroid.planner.domain.HierarchyInfo;
import com.drk.tools.contextandroid.planner.domain.TextInfo;
import com.drk.tools.contextandroid.planner.variables.*;

import java.util.HashMap;
import java.util.LinkedHashMap;

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

    }

    TextInfo getTextInfo() {

    }

    BackInfo getBackInfo() {

    }

    ActionInfo getActionInfo() {

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
            for (ViewInfo info : screenInfo.views) {
                mapElements.put(Element.values()[mapElements.size()], info);
            }
            for (PagerInfo pagerInfo : screenInfo.pagers) {
                mapPagers.put(PagerElement.values()[mapPagers.size()], pagerInfo);
            }
            return this;
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
