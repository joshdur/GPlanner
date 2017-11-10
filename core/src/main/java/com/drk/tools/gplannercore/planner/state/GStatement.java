package com.drk.tools.gplannercore.planner.state;

import com.drk.tools.gplannercore.planner.state.atoms.Atom;
import com.drk.tools.gplannercore.planner.state.atoms.BinaryAtom;
import com.drk.tools.gplannercore.planner.state.atoms.TernaryAtom;
import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.core.variables.Variable;

public class GStatement implements Statement {

    private final int hashCode;
    private final int hashCodeInverse;
    private final boolean isNegated;

    private GStatement(int hashCode, int hashCodeInverse, boolean isNegated){
        this.hashCode = hashCode;
        this.hashCodeInverse = hashCodeInverse;
        this.isNegated = isNegated;
    }

    public static <V extends Variable> GStatement from(Atom<V> atom, V variable) {
        int hash = GCodes.hash(atom, variable);
        int inverse = GCodes.multiplicativeInverse(hash);
        return new GStatement(hash, inverse, false);
    }

    public static <V1 extends Variable, V2 extends Variable> GStatement from(BinaryAtom<V1, V2> atom, V1 v1, V2 v2){
        int hash = GCodes.hash(atom, v1, v2);
        int inverse = GCodes.multiplicativeInverse(hash);
        return new GStatement(hash, inverse, false);
    }

    public static <V1 extends Variable, V2 extends Variable, V3 extends Variable> GStatement from(TernaryAtom<V1, V2, V3> atom, V1 v1, V2 v2, V3 v3){
        int hash = GCodes.hash(atom, v1, v2, v3);
        int inverse = GCodes.multiplicativeInverse(hash);
        return new GStatement(hash, inverse, false);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Statement){
            Statement other = (Statement) obj;
            return isNegated == other.isNegated() && hashCode() == other.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return isNegated ? this.hashCodeInverse : this.hashCode;
    }

    @Override
    public Statement not() {
        return new GStatement(hashCode, hashCodeInverse, !isNegated);
    }

    @Override
    public boolean isNegated(){
        return isNegated;
    }

}
