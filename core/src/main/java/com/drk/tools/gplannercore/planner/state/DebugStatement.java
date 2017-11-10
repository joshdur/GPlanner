package com.drk.tools.gplannercore.planner.state;

import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.core.variables.Variable;
import com.drk.tools.gplannercore.planner.state.atoms.Atom;
import com.drk.tools.gplannercore.planner.state.atoms.BinaryAtom;
import com.drk.tools.gplannercore.planner.state.atoms.TernaryAtom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DebugStatement implements Statement {

    private final Atom atom;
    private final int hashCode;
    private final boolean isNegated;
    private final Variable[] variables;

    private DebugStatement(Atom atom, int hashCode, boolean isNegated, Variable... variables) {
        this.atom = atom;
        this.hashCode = hashCode;
        this.isNegated = isNegated;
        this.variables = variables;
    }

    public static <V extends Variable> DebugStatement from(Atom<V> atom, V variable) {
        return new DebugStatement(atom, GCodes.hash(atom, variable), false, variable);
    }

    public static <V1 extends Variable, V2 extends Variable> DebugStatement from(BinaryAtom<V1, V2> atom, V1 v1, V2 v2) {
        return new DebugStatement(atom, GCodes.hash(atom, v1, v2), false, v1, v2);
    }

    public static <V1 extends Variable, V2 extends Variable, V3 extends Variable> DebugStatement from(TernaryAtom<V1, V2, V3> atom, V1 v1, V2 v2, V3 v3) {
        return new DebugStatement(atom, GCodes.hash(atom, v1, v2, v3), false, v1, v2, v3);
    }

    @Override
    public Statement not() {
        return new DebugStatement(atom, GCodes.multiplicativeInverse(hashCode), !isNegated, variables);
    }

    @Override
    public boolean isNegated() {
        return isNegated;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Statement) {
            Statement other = (Statement) obj;
            return isNegated == other.isNegated() && hashCode == other.hashCode();
        }
        return false;
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
        return (isNegated ? "¬ " : "") + atom.getClass().getSimpleName() + " [" + variableList.toString() + "] ";
    }
}
