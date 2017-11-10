package com.drk.tools.gplannercore.planner.search.unifier;

import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.state.Statement;

import java.util.Set;

public interface UnifierBuilder {

    SearchUnifier from(State state);
}
