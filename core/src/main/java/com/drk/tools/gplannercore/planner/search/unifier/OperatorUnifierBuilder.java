package com.drk.tools.gplannercore.planner.search.unifier;

import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.main.BaseUnifier;
import com.drk.tools.gplannercore.core.state.State;

import java.util.List;

public class OperatorUnifierBuilder implements UnifierBuilder {

    private final Context context;

    public OperatorUnifierBuilder(Context context) {
        this.context = context;
    }

    @Override
    public SearchUnifier from(State state) {
        return OperatorUnifier.from(context, state);
    }

}
