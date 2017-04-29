package com.drk.tools.contextandroid.domain;

public class ViewInfo {

    public final int id;
    public final String text;
    public final Action action;

    private ViewInfo(Builder builder) {
        this.id = builder.id;
        this.text = builder.text;
        this.action = builder.action;
    }

    public boolean hasClickDefined() {
        return action != null;
    }

    public static Builder builder(int id) {
        return new Builder(id);
    }

    public static class Builder {

        private final int id;
        private String text;
        private Action action;

        Builder(int id) {
            this.id = id;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder click(Action action) {
            this.action = action;
            return this;
        }

        public ViewInfo build() {
            return new ViewInfo(this);
        }
    }


}
