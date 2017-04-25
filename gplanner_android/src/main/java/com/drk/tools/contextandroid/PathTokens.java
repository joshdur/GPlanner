package com.drk.tools.contextandroid;

import java.util.HashSet;
import java.util.Set;

public class PathTokens {

    final Set<IdText> textToCheck;
    final Set<IdText> textToInput;
    final Set<String> ats;
    final Set<Integer> clickeds;
    final Set<Enum> mockInjections;
    final Set<Enum> mockRequests;

    private PathTokens(Builder builder) {
        textToCheck = builder.textToCheck;
        textToInput = builder.textToInput;
        ats = builder.ats;
        clickeds = builder.clickeds;
        mockInjections = builder.mockInjections;
        mockRequests = builder.mockRequests;
    }

    public static class Builder {

        final Set<IdText> textToCheck = new HashSet<>();
        final Set<IdText> textToInput = new HashSet<>();
        final Set<String> ats = new HashSet<>();
        final Set<Integer> clickeds = new HashSet<>();
        final Set<Enum> mockInjections = new HashSet<>();
        final Set<Enum> mockRequests = new HashSet<>();

        public Builder checkText(int resId, String text) {
            textToCheck.add(new IdText(resId, text));
            return this;
        }

        public Builder setText(int resId, String text) {
            textToInput.add(new IdText(resId, text));
            return this;
        }

        public Builder at(String screenName) {
            ats.add(screenName);
            return this;
        }

        public Builder clicked(int resId) {
            clickeds.add(resId);
            return this;
        }

        public Builder mockRequest(Enum e) {
            mockRequests.add(e);
            return this;
        }

        public Builder mockInjection(Enum e) {
            mockInjections.add(e);
            return this;
        }

        public PathTokens build() {
            return new PathTokens(this);
        }
    }
}
