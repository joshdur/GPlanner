package com.drk.tools.gplannercore.planner.search.unifier;

import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.main.BaseUnifier;
import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.core.state.Transition;

import java.util.List;
import java.util.Set;

public class OperatorUnifier extends SearchUnifier {

    private final List<BaseUnifier> unifiers;

    public OperatorUnifier(Context context, Set<Statement> state){
        super(state);
        unifiers = context.getUnifiers();
    }

    private boolean hasNext() {
        return !unifiers.isEmpty() && unifiers.get(0).hasNext();
    }

    @Override
    public Transition next() {
        while(hasNext()){
            Transition transition = internalNext();
            if(isApplicable(transition)){
                return transition;
            }
        }
        return null;
    }

    private Transition internalNext(){
        BaseUnifier unifier = unifiers.get(0);
        Transition transition = unifier.next();
        if(!unifier.hasNext()){
            unifiers.remove(unifier);
        }
        return transition;
    }
}
