package com.drk.tools.gplannercore.planner.state;

import com.drk.tools.gplannercore.core.Atom;
import com.drk.tools.gplannercore.core.state.Statement;

public class GStatement implements Statement {

    private final int hashCode;

    private GStatement(int hashCode){
        this.hashCode = hashCode;
    }

    public static GStatement from(Atom atom, Enum variable){
        int hashCode = 31 * atom.getClass().hashCode() + variable.hashCode();
        return new GStatement(hashCode);
    }

    public static GStatement from(int hashCode){
        return new GStatement(hashCode);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Statement && hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }
}
