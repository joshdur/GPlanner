package com.drk.tools.gplannercore.planner.search.unifier;

import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.main.BaseUnifier;
import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.core.state.Transition;

import java.util.List;
import java.util.Set;

public class OperatorUnifierBuilder implements UnifierBuilder {

    private final Context context;

    public OperatorUnifierBuilder(Context context) {
        this.context = context;
    }

    @Override
    public SearchUnifier from(Set<Statement> positive, Set<Statement> negative) {
        List<BaseUnifier> unifiers = context.getValidUnifiers();
        Application application = new Application(positive, negative);
        return new OperatorUnifier(unifiers, application);
    }

    public class OperatorUnifier extends SearchUnifier {

        private final List<BaseUnifier> unifiers;

        OperatorUnifier(List<BaseUnifier> unifiers, Application application) {
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
}
