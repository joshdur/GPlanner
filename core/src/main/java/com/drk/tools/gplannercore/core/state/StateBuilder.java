package com.drk.tools.gplannercore.core.state;

import com.drk.tools.gplannercore.core.Atom;

public interface StateBuilder {

    <E extends Enum> StateBuilder set(Atom<E> a, E v);

    State build();
}
