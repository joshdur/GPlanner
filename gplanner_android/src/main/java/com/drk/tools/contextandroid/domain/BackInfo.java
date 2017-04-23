package com.drk.tools.contextandroid.domain;

import com.drk.tools.contextandroid.variables.Screen;
import com.drk.tools.gplannercore.core.state.Statement;

import java.util.Set;

public class BackInfo {
    public boolean isBackInfoDefined(Screen screen) {
        return false;
    }

    public BackData backOf(Screen screen) {
        return null;
    }

    public static class BackData {

        public final Set<Statement> preconds;
        public final Set<Statement> positiveEffects;
        public final Set<Statement> negativeEffects;

        public BackData(Set<Statement> preconds, Set<Statement> positiveEffects, Set<Statement> negativeEffects) {
            this.preconds = preconds;
            this.positiveEffects = positiveEffects;
            this.negativeEffects = negativeEffects;
        }
    }
}
