package com.drk.tools.contextandroid.domain;

import java.util.ArrayList;
import java.util.List;

public class ScreenInfo {

    public final String name;
    public final List<ViewInfo> views;
    public final List<PagerInfo> pagers;
    public final NavigationInfo back;

    private ScreenInfo(Builder builder) {
        this.name = builder.name;
        this.views = builder.views;
        this.pagers = builder.pagers;
        this.back = builder.back;
    }

    public boolean hashBackInfo() {
        return back != null;
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static class Builder {

        private final String name;
        private List<ViewInfo> views;
        private List<PagerInfo> pagers;
        private NavigationInfo back;

        Builder(String name) {
            this.name = name;
            this.views = new ArrayList<>();
            this.pagers = new ArrayList<>();
        }

        public Builder addView(ViewInfo viewInfo) {
            views.add(viewInfo);
            return this;
        }

        public Builder addPager(PagerInfo pagerInfo) {
            pagers.add(pagerInfo);
            return this;
        }

        public Builder back(NavigationInfo navigationInfo) {
            this.back = navigationInfo;
            return this;
        }

        public ScreenInfo build() {
            return new ScreenInfo(this);
        }


    }
}
