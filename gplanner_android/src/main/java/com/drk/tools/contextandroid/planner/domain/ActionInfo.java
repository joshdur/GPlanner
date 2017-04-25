package com.drk.tools.contextandroid.planner.domain;

import com.drk.tools.contextandroid.planner.variables.Element;
import com.drk.tools.gplannercore.core.state.Statement;

import java.util.HashMap;
import java.util.Set;

public class ActionInfo {

    private final HashMap<Element, ActionData> hashActionData;

    public ActionInfo(HashMap<Element, ActionData> hashActionData) {
        this.hashActionData = hashActionData;
    }

    public boolean isActionDefined(Element element) {
        return hashActionData.containsKey(element);
    }

    public ActionData actionOf(Element element) {
        return hashActionData.get(element);
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
