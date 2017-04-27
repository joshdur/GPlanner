package com.drk.tools.contextandroid.domain;

import java.util.HashSet;
import java.util.Set;

public class Scenario {

    public final Set<ElementText> textToCheck;
    public final Set<ElementText> textToInput;
    public final Set<String> ats;
    public final Set<Integer> clickeds;
    public final Enum mock;

    private Scenario(Builder builder) {
        textToCheck = builder.textToCheck;
        textToInput = builder.textToInput;
        ats = builder.ats;
        clickeds = builder.clickeds;
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
        private final Set<ElementText> textToInput = new HashSet<>();
        private final Set<String> ats = new HashSet<>();
        private final Set<Integer> clickeds = new HashSet<>();
        private Enum mock;

        public Builder checkText(int resId, String text) {
            textToCheck.add(new ElementText(resId, text));
            return this;
        }

        public Builder setText(int resId, String text) {
            textToInput.add(new ElementText(resId, text));
            return this;
        }

        public Builder checkAt(String screenName) {
            ats.add(screenName);
            return this;
        }

        public Builder clicked(int resId) {
            clickeds.add(resId);
            return this;
        }

        public Builder mock(Enum e) {
            mock = e;
            return this;
        }

        public Scenario build() {
            return new Scenario(this);
        }
    }
}
