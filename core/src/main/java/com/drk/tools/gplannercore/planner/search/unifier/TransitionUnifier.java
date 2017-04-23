package com.drk.tools.gplannercore.planner.search.unifier;

import com.drk.tools.gplannercore.core.state.Transition;

import java.util.List;

public class TransitionUnifier extends SearchUnifier {

    private final List<Transition> transitions;
    private int index;

    private static int ID = 0;
    private int currentId;

    private static int getID(){
        int id = ID;
        ID ++;
        return id;
    }

    public TransitionUnifier(List<Transition> transitions, Application application) {
        super(application);
        this.transitions = transitions;
        this.index = 0;
        this.currentId = getID();
    }

    private boolean hasNext() {
        return index < transitions.size();
    }

    @Override
    public Transition next() {
        if(currentId == 0){
            System.out.print("");
        }
        while (hasNext()) {
            Transition transition = transitions.get(index);
            index++;
            if (application.isApplicable(transition)) {
                return transition;
            }
        }
        return null;
    }

}
