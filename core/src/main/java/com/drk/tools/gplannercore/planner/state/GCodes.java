package com.drk.tools.gplannercore.planner.state;

import com.drk.tools.gplannercore.core.variables.Variable;
import com.drk.tools.gplannercore.planner.state.atoms.Atom;
import com.drk.tools.gplannercore.planner.state.atoms.BinaryAtom;
import com.drk.tools.gplannercore.planner.state.atoms.TernaryAtom;

class GCodes {

    public static int multiplicativeInverse(int hash) {
        int inv = hash;
        inv = inv(hash, inv);
        inv = inv(hash, inv); //8 bits
        inv = inv(hash, inv); //16 bits
        inv = inv(hash, inv); //32 bits
        return inv;
    }

    private static int inv(int x, int y) {
        return y * (2 - y * x);
    }

    public static <V extends Variable> int hash(Atom<V> atom, V variable) {
        int h = 31 * atom.getClass().getCanonicalName().hashCode() + variable.hashCode();
        return isOdd(h) ? h : h + 1;
    }

    public static <V1 extends Variable, V2 extends Variable> int hash(BinaryAtom<V1, V2> atom, V1 v1, V2 v2) {
        int h = 47 * atom.getClass().getCanonicalName().hashCode() + v1.hashCode() + v2.hashCode();
        return isOdd(h) ? h : h + 1;
    }

    public static <V1 extends Variable, V2 extends Variable, V3 extends Variable> int hash(TernaryAtom<V1, V2, V3> atom, V1 v1, V2 v2, V3 v3) {
        int h = 67 * atom.getClass().getCanonicalName().hashCode() + v1.hashCode() + v2.hashCode() + v3.hashCode();
        return isOdd(h) ? h : h + 1;
    }

    private static boolean isOdd(int hash) {
        return hash % 2 != 0;
    }
}
