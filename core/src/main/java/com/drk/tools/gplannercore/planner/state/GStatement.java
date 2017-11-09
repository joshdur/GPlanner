package com.drk.tools.gplannercore.planner.state;

import com.drk.tools.gplannercore.planner.state.atoms.Atom;
import com.drk.tools.gplannercore.planner.state.atoms.BinaryAtom;
import com.drk.tools.gplannercore.planner.state.atoms.TernaryAtom;
import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.core.variables.Variable;

public class GStatement implements Statement {

    private final int hashCode;

    private GStatement(int hashCode){
        this.hashCode = hashCode;
    }

    public static <V extends Variable> GStatement from(Atom<V> atom, V variable) {
        int hashCode = 31 * atom.getClass().getCanonicalName().hashCode() + variable.hashCode();
        return new GStatement(hashCode);
    }

    public static <V1 extends Variable, V2 extends Variable> GStatement from(BinaryAtom<V1, V2> atom, V1 v1, V2 v2){
        int hashCode = 47 * atom.getClass().getCanonicalName().hashCode() + v1.hashCode() + v2.hashCode();
        return new GStatement(hashCode);
    }

    public static <V1 extends Variable, V2 extends Variable, V3 extends Variable> GStatement from(TernaryAtom<V1, V2, V3> atom, V1 v1, V2 v2, V3 v3){
        int hashCode = 67 * atom.getClass().getCanonicalName().hashCode() + v1.hashCode() + v2.hashCode() + v3.hashCode();
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
