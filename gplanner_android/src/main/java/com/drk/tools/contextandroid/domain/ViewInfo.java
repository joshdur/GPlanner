package com.drk.tools.contextandroid.domain;

public class ViewInfo {

    public final int id;
    public final String text;
    public final NavigationInfo navigationInfo;

    private ViewInfo(Builder builder) {
        this.id = builder.id;
        this.text = builder.text;
        this.navigationInfo = builder.navigationInfo;
    }

    public boolean hasClickDefined() {
        return navigationInfo != null;
    }

    public static Builder builder(int id) {
        return new Builder(id);
    }

    public static class Builder {

        private final int id;
        private String text;
        private NavigationInfo navigationInfo;

        Builder(int id) {
            this.id = id;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder clickActionInfo(NavigationInfo navigationInfo) {
            this.navigationInfo = navigationInfo;
            return this;
        }

        public ViewInfo build() {
            return new ViewInfo(this);
        }
    }


}
