package com.drk.tools.gplannercore.core.search.context;

import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.search.SearchException;
import com.drk.tools.gplannercore.core.state.State;
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
            applySystemEffects(newState, transition);
        }
        newState.applyAll(transition.getEffects());
        return newState;
    }

    private void applySystemEffects(State state, Transition transition) throws SearchException {
        try {
            Transition eventTransition = context.execute(transition);
            state.applyAll(eventTransition.getEffects());
        } catch (Throwable e) {
            throw new SearchException(e);
        }
    }

}
