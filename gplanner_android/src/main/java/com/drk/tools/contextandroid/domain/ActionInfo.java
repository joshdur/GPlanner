package com.drk.tools.contextandroid.domain;

import com.drk.tools.contextandroid.variables.Element;
import com.drk.tools.gplannercore.core.state.Statement;

import java.util.Set;

public class ActionInfo {


    public boolean isActionDefined(Element element) {
        return false;
    }

    public ActionData actionOf(Element element) {
        return null;
    }

    public static class ActionData {

        public final Set<Statement> preconds;
        public final Set<Statement> positiveEffects;
        public final Set<Statement> negativeEffects;

        public ActionData(Set<Statement> preconds, Set<Statement> positiveEffects, Set<Statement> negativeEffects) {
            this.preconds = preconds;
            this.positiveEffects = positiveEffects;
            this.negativeEffects = negativeEffects;
        }
    }
}
