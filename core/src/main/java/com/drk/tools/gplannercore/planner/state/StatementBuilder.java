package com.drk.tools.gplannercore.planner.state;

import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.core.variables.Variable;
import com.drk.tools.gplannercore.core.variables.VariableRange;
import com.drk.tools.gplannercore.planner.state.atoms.Atom;
import com.drk.tools.gplannercore.planner.state.atoms.BinaryAtom;
import com.drk.tools.gplannercore.planner.state.atoms.TernaryAtom;

import java.util.HashSet;
import java.util.Set;

class StatementBuilder {

    public static <V extends Variable> Statement build(boolean debug, Atom<V> a, V v) {
        return debug ? DebugStatement.from(a, v) : GStatement.from(a, v);
    }

    public static <V1 extends Variable, V2 extends Variable> Statement build(boolean debug, BinaryAtom<V1, V2> a, V1 v1, V2 v2) {
        return debug ? DebugStatement.from(a, v1, v2) : GStatement.from(a, v1, v2);
    }

    public static <V1 extends Variable, V2 extends Variable, V3 extends Variable> Statement build(boolean debug, TernaryAtom<V1, V2, V3> a, V1 v1, V2 v2, V3 v3) {
        return debug ? DebugStatement.from(a, v1, v2, v3) : GStatement.from(a, v1, v2, v3);
    }

    public static <V extends Variable> Set<Statement> buildOthers(Context context, Atom<V> a, V v) {
        Set<Statement> otherStatements = new HashSet<>();
        for (V variable : getVariables(context, v)) {
            if (!variable.equals(v)) {
                otherStatements.add(build(context.isDebug(), a, variable));
            }
        }
        return otherStatements;
    }

    public static <V1 extends Variable, V2 extends Variable> Set<Statement> buildOthers(Context context, BinaryAtom<V1, V2> a, V1 v1, V2 v2) {
        Set<Statement> otherStatements = new HashSet<>();
        for (V1 variable : getVariables(context, v1)) {
            for (V2 variable2 : getVariables(context, v2)) {
                if (!(variable.equals(v1) && variable.equals(v2))) {
                    otherStatements.add(build(context.isDebug(), a, variable, variable2));
                }
            }
        }
        return otherStatements;
    }


    public static <V1 extends Variable, V2 extends Variable, V3 extends Variable> Set<Statement> buildOthers(Context context, TernaryAtom<V1, V2, V3> a, V1 v1, V2 v2, V3 v3) {
        Set<Statement> otherStatements = new HashSet<>();
        for (V1 variable : getVariables(context, v1)) {
            for (V2 variable2 : getVariables(context, v2)) {
                for (V3 variable3 : getVariables(context, v3)) {
                    if (!(variable.equals(v1) && variable.equals(v2) && variable.equals(v3))) {
                        otherStatements.add(build(context.isDebug(), a, variable, variable2, variable3));
                    }
                }
            }
        }
        return otherStatements;
    }

    private static <V extends Variable> Set<V> getVariables(Context context, V v) {
        Set<V> set = new HashSet<>();
        VariableRange<V> variableRange = context.injectRangeFromVariableClass(v);
        for (int i = 0; i < variableRange.count(); i++) {
            V otherVariable = variableRange.variableAt(i);
            set.add(otherVariable);
        }
        return set;
    }

}
