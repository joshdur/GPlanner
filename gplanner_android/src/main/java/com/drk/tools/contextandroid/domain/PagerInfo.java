package com.drk.tools.contextandroid.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class PagerInfo {

    public final int id;
    public final int pages;
    public final HashMap<Integer, List<ViewInfo>> views;

    private PagerInfo(Builder builder) {
        this.id = builder.id;
        this.pages = builder.pages;
        this.views = builder.views;
    }

    public static Builder builder(int id, int pages) {
        return new Builder(id, pages);
    }

    public static class Builder {

        private int id;
        private int pages;
        private HashMap<Integer, List<ViewInfo>> views;

        private Builder(int id, int pages) {
            this.id = id;
            this.pages = pages;
            this.views = new LinkedHashMap<>();
        }

        public Builder addView(int page, ViewInfo viewInfo) {
            List<ViewInfo> viewInfos = views.get(page);
            if (viewInfos == null) {
                viewInfos = new ArrayList<>();
                views.put(page, viewInfos);
            }
            viewInfos.add(viewInfo);
            return this;
        }

        public PagerInfo build() {
            return new PagerInfo(this);
        }
    }
}
