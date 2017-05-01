package com.drk.tools.contextandroid.domain;

public class ViewInfo {

    public final int id;
    public final String text;
    public final String hint;
    public final Action clickAction;
    public final Action imeOptionsClickAction;
    public final ViewInfo showsAfter;

    private ViewInfo(Builder builder) {
        this.id = builder.id;
        this.text = builder.text;
        this.hint = builder.hint;
        this.clickAction = builder.action;
        this.imeOptionsClickAction = builder.imeOptionsClickAction;
        this.showsAfter = builder.showsAfter;
    }

    public boolean matches(ViewInfo viewInfo) {
        if (id != -1 && viewInfo.id != -1) {
            return id == viewInfo.id;
        }
        if (text != null && viewInfo.text != null) {
            return text.equals(viewInfo.text);
        }
        if (hint != null && viewInfo.hint != null) {
            return hint.equals(viewInfo.hint);
        }
        return false;
    }

    public boolean hasClickDefined() {
        return clickAction != null;
    }

    public boolean hasImeOptionsClickDefined() {
        return imeOptionsClickAction != null;
    }

    public boolean dependsOnView() {
        return showsAfter != null;
    }

    public boolean hasId() {
        return id != -1;
    }

    public static ViewInfo of(int id) {
        return builder().id(id).build();
    }

    public static ViewInfo of(int id, ViewInfo showsAfter) {
        return builder().id(id).showsAfter(showsAfter).build();
    }

    @Override
    public String toString() {
        return String.format("ViewInfo with id:%d, text:%s, hint:%s", id, text, hint);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private int id = -1;
        private String text;
        private String hint;
        private Action action;
        private Action imeOptionsClickAction;
        private ViewInfo showsAfter;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder hint(String hint) {
            this.hint = hint;
            return this;
        }

        public Builder click(Action action) {
            this.action = action;
            return this;
        }

        public Builder imeOptionsClickAction(Action action) {
            this.imeOptionsClickAction = action;
            return this;
        }

        public Builder showsAfter(ViewInfo viewInfo) {
            this.showsAfter = viewInfo;
            return this;
        }

        public ViewInfo build() {
            return new ViewInfo(this);
        }
    }


}
