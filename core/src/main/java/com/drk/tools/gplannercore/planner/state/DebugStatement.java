package com.drk.tools.gplannercore.planner.state;

import com.drk.tools.gplannercore.planner.state.atoms.Atom;
import com.drk.tools.gplannercore.planner.state.atoms.BinaryAtom;
import com.drk.tools.gplannercore.planner.state.atoms.TernaryAtom;
import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.core.variables.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DebugStatement implements Statement {

    private final Atom atom;
    private final int hashCode;
    private final Variable[] variables;

    private DebugStatement(Atom atom, int hashCode, Variable... variables) {
        this.atom = atom;
        this.hashCode = hashCode;
        this.variables = variables;
    }

    public static <V extends Variable> DebugStatement from(Atom<V> atom, V variable) {
        int hashCode = 31 * atom.getClass().hashCode() + variable.hashCode();
        return new DebugStatement(atom, hashCode, variable);
    }

    public static <V1 extends Variable, V2 extends Variable> DebugStatement from(BinaryAtom<V1, V2> atom, V1 v1, V2 v2) {
        int hashCode = 47 * atom.getClass().hashCode() + v1.hashCode() + v2.hashCode();
        return new DebugStatement(atom, hashCode, v1, v2);
    }

    public static <V1 extends Variable, V2 extends Variable, V3 extends Variable> DebugStatement from(TernaryAtom<V1, V2, V3> atom, V1 v1, V2 v2, V3 v3) {
        int hashCode = 67 * atom.getClass().hashCode() + v1.hashCode() + v2.hashCode() + v3.hashCode();
        return new DebugStatement(atom, hashCode, v1, v2, v3);
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
        List<Variable> variableList = new ArrayList<>();
        if (variables != null) {
            variableList.addAll(Arrays.asList(variables));
        }
        return atom.getClass().getSimpleName() + " [" + variableList.toString() + "] ";
    }
}
