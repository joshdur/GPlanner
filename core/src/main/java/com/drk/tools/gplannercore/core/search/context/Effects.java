package com.drk.tools.gplannercore.core.search.context;

import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.search.SearchException;
import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.core.state.Transition;

class Effects {

    private final Context context;
    private final boolean online;

    Effects(Context context, boolean online) {
        this.context = context;
        this.online = online;
    }

    State applyEffects(State state, Transition transition) throws SearchException {
        State newState = state.clone();
        if (online) {
            applySystemEffects(state, transition);
        }
        applyStateEffects(newState, transition.stateTransition);
        return newState;
    }

    private void applySystemEffects(State state, Transition transition) throws SearchException {
        try {
            Transition eventTransition = context.execute(transition);
            applyStateEffects(state, eventTransition.stateTransition);
        } catch (Throwable e) {
            throw new SearchException(e);
        }
    }

    private void applyStateEffects(State state, StateTransition stateTransition) {
        for (Statement negative : stateTransition.getNegativeEffects()) {
            state.remove(negative);
        }
        for (Statement positive : stateTransition.getPositiveEffects()) {
            state.set(positive);
        }
    }
}
