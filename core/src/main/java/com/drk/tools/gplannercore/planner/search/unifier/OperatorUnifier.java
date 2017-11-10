package com.drk.tools.gplannercore.planner.search.unifier;

import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.main.BaseUnifier;
import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.core.state.Transition;
import com.drk.tools.gplannercore.planner.state.GState;

import java.util.Collection;
import java.util.List;

public class OperatorUnifier extends SearchUnifier {

    private final List<BaseUnifier> unifiers;

    public static OperatorUnifier from(Context context, State state) {
        List<BaseUnifier> unifiers = context.getValidUnifiers();
        Application application = new Application(state);
        return new OperatorUnifier(unifiers, application);
    }

    private OperatorUnifier(List<BaseUnifier> unifiers, Application application) {
        super(application);
        this.unifiers = unifiers;
    }

    private boolean hasNext() {
        return !unifiers.isEmpty() && unifiers.get(0).hasNext();
    }

    @Override
    public Transition next() {
        while (hasNext()) {
            Transition transition = internalNext();
            if (application.isApplicable(transition)) {
                return transition;
            }
        }
        return null;
    }

    private Transition internalNext() {
        BaseUnifier unifier = unifiers.get(0);
        Transition transition = unifier.next();
        if (!unifier.hasNext()) {
            unifiers.remove(unifier);
        }
        return transition;
    }
}
