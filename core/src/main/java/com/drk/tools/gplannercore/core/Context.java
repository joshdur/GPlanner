package com.drk.tools.gplannercore.core;

import com.drk.tools.gplannercore.core.main.BaseUnifier;
import com.drk.tools.gplannercore.core.state.Transition;

import java.util.List;

public abstract class Context {

    protected final Bundle bundle;

    public Context(Bundle bundle) {
        this.bundle = bundle;
    }

    public abstract List<BaseUnifier> getUnifiers();

    public String asString(Plan plan) {
        StringBuilder sb = new StringBuilder();
        for (Transition transition : plan.transitions) {
            sb.append(asString(transition));
            sb.append(" | ");
        }
        sb.append("\n");
        return sb.toString();
    }

    public String asString(Transition transition) {
        BaseUnifier unifier = findUnifier(transition);
        return unifier != null ? unifier.asString(transition) : "";
    }

    private BaseUnifier findUnifier(Transition transition) {
        List<BaseUnifier> unifiers = getUnifiers();
        for (BaseUnifier baseUnifier : unifiers) {
            if (baseUnifier.getCode() == transition.unifierCode) {
                return baseUnifier;
            }
        }
        throw new IllegalStateException("Unifier for transition not found");
    }

    public void execute(Plan plan) throws Throwable {
        for (Transition transition : plan.transitions) {
            execute(transition);
        }
    }

    public Transition execute(Transition transition) throws Throwable {
        BaseUnifier unifier = findUnifier(transition);
        return unifier.execute(transition);
    }

}
