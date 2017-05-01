package com.drk.tools.contextandroid.domain;

import java.util.HashSet;
import java.util.Set;

public class Scenario {

    public final Set<ElementText> textToCheck;
    public final Set<ElementInputText> textToInput;
    public final Set<String> ats;
    public final Set<ViewInfo> clickeds;
    public final Set<ElementState> elementStates;
    public final Enum mock;

    private Scenario(Builder builder) {
        textToCheck = builder.textToCheck;
        textToInput = builder.textToInput;
        ats = builder.ats;
        clickeds = builder.clickeds;
        elementStates = builder.elementStates;
        mock = builder.mock;
    }

    public boolean shouldMock() {
        return mock != null;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Set<ElementText> textToCheck = new HashSet<>();
        private final Set<ElementInputText> textToInput = new HashSet<>();
        private final Set<String> ats = new HashSet<>();
        private final Set<ViewInfo> clickeds = new HashSet<>();
        private final Set<ElementState> elementStates = new HashSet<>();
        private Enum mock;

        public Builder withCheckedText(ViewInfo viewInfo, String text) {
            textToCheck.add(new ElementText(viewInfo, text, false, false));
            return this;
        }

        public Builder withContainedText(ViewInfo viewInfo, String text) {
            textToCheck.add(new ElementText(viewInfo, text, false, true));
            return this;
        }
        public Builder withCheckedTextForAll(ViewInfo viewInfo, String text) {
            textToCheck.add(new ElementText(viewInfo, text, true, false));
            return this;
        }

        public Builder withContainedTextForAll(ViewInfo viewInfo, String text) {
            textToCheck.add(new ElementText(viewInfo, text, true, true));
            return this;
        }

        public Builder withInputText(ViewInfo viewInfo, String text, boolean pressImeOptions) {
            textToInput.add(new ElementInputText(viewInfo, text, pressImeOptions));
            return this;
        }

        public Builder withCheckedScreen(String screenName) {
            ats.add(screenName);
            return this;
        }

        public Builder withElementClicked(int resId) {
            clickeds.add(ViewInfo.of(resId));
            return this;
        }

        public Builder withElementClicked(ViewInfo viewInfo){
            clickeds.add(viewInfo);
            return this;
        }

        public Builder withMocked(Enum e) {
            mock = e;
            return this;
        }

        public Builder withElementState(int resId, ElementState.State state) {
            elementStates.add(new ElementState(ViewInfo.of(resId), state));
            return this;
        }

        public Builder withElementState(ViewInfo info, ElementState.State state) {
            elementStates.add(new ElementState(info, state));
            return this;
        }

        public Scenario build() {
            return new Scenario(this);
        }
    }
}
