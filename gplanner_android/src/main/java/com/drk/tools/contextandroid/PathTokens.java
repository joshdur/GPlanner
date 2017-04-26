package com.drk.tools.contextandroid;

import java.util.HashSet;
import java.util.Set;

public class PathTokens {

    final Set<IdText> textToCheck;
    final Set<IdText> textToInput;
    final Set<String> ats;
    final Set<Integer> clickeds;
    final Enum mock;

    private PathTokens(Builder builder) {
        textToCheck = builder.textToCheck;
        textToInput = builder.textToInput;
        ats = builder.ats;
        clickeds = builder.clickeds;
        mock = builder.mock;
    }

    boolean shouldMock() {
        return mock != null;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Set<IdText> textToCheck = new HashSet<>();
        private final Set<IdText> textToInput = new HashSet<>();
        private final Set<String> ats = new HashSet<>();
        private final Set<Integer> clickeds = new HashSet<>();
        private Enum mock;

        public Builder checkText(int resId, String text) {
            textToCheck.add(new IdText(resId, text));
            return this;
        }

        public Builder setText(int resId, String text) {
            textToInput.add(new IdText(resId, text));
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

        public PathTokens build() {
            return new PathTokens(this);
        }
    }
}
