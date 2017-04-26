package com.drk.tools.gplannercore.planner.state.debug;

import com.drk.tools.gplannercore.core.Atom;
import com.drk.tools.gplannercore.core.state.Statement;

public class DebugStatement implements Statement {

    private final Atom atom;
    private final Enum variable;
    private final int hashCode;

    private DebugStatement(Atom atom, Enum variable, int hashCode) {
        this.atom = atom;
        this.variable = variable;
        this.hashCode = hashCode;
    }

    public static DebugStatement from(Atom atom, Enum variable) {
        int hashCode = 31 * atom.getClass().hashCode() + variable.hashCode();
        return new DebugStatement(atom, variable, hashCode);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Statement && hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public String toString() {
        return atom.getClass().getSimpleName() + " [" + variable.name() + "] ";
    }
}
