package com.drk.tools.contextandroid.domain;

public class ViewInfo {

    public final int id;
    public final String text;
    public final String inputText;
    public final ClickActionInfo clickActionInfo;

    private ViewInfo(Builder builder) {
        this.id = builder.id;
        this.text = builder.text;
        this.inputText = builder.inputText;
        this.clickActionInfo = builder.clickActionInfo;
    }

    public static Builder builder(int id) {
        return new Builder(id);
    }

    public static class Builder {

        private final int id;
        private String text;
        private String inputText;
        private ClickActionInfo clickActionInfo;

        Builder(int id) {
            this.id = id;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder inputText(String inputText) {
            this.inputText = inputText;
            return this;
        }

        public Builder clickActionInfo(ClickActionInfo clickActionInfo) {
            this.clickActionInfo = clickActionInfo;
            return this;
        }

        public ViewInfo build() {
            return new ViewInfo(this);
        }
    }


}
