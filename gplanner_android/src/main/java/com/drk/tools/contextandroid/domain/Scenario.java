package com.drk.tools.contextandroid.domain;

import java.util.HashSet;
import java.util.Set;

public class Scenario {

    public final Set<ElementText> textToCheck;
    public final Set<ElementInputText> textToInput;
    public final Set<String> ats;
    public final Set<Integer> clickeds;
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
        private final Set<Integer> clickeds = new HashSet<>();
        private final Set<ElementState> elementStates = new HashSet<>();
        private Enum mock;

        public Builder withCheckedText(int resId, String text) {
            textToCheck.add(new ElementText(resId, text, false, false));
            return this;
        }

        public Builder withContainedText(int resId, String text) {
            textToCheck.add(new ElementText(resId, text, false, true));
            return this;
        }
        public Builder withCheckedTextForAll(int resId, String text) {
            textToCheck.add(new ElementText(resId, text, true, false));
            return this;
        }

        public Builder withContainedTextForAll(int resId, String text) {
            textToCheck.add(new ElementText(resId, text, true, true));
            return this;
        }

        public Builder withInputText(int resId, String text) {
            textToInput.add(new ElementInputText(resId, text, false));
            return this;
        }

        public Builder withCheckedScreen(String screenName) {
            ats.add(screenName);
            return this;
        }

        public Builder withElementClicked(int resId) {
            clickeds.add(resId);
            return this;
        }

        public Builder withMocked(Enum e) {
            mock = e;
            return this;
        }

        public Builder withElementState(int resId, ElementState.State state) {
            elementStates.add(new ElementState(resId, state));
            return this;
        }

        public Scenario build() {
            return new Scenario(this);
        }
    }
}
