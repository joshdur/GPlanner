package com.drk.tools.gplannersample;

import com.drk.tools.gplannercore.annotations.SystemAction;
import com.drk.tools.gplannercore.core.main.SystemActions;
import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannersample.Monkey.*;

public class MonkeySystem extends SystemActions {

    @SystemAction
    public StateTransition climbDown(Location x) {
        System.out.print(" climbdown " + x.name() );
        return null;
    }

    @SystemAction
    public StateTransition climbUp(Location x) {
        System.out.print(" climbUp " + x.name() );
        return null;
    }

    @SystemAction
    public StateTransition moveBox(Location x, Location y) {
        System.out.print(" moveBox " + x.name() + " " + y.name());
        return null;
    }

    @SystemAction
    public StateTransition move(Location x, Location y) {
        System.out.print(" move " + x.name() + " " + y.name());
        return null;
    }

    @SystemAction
    public StateTransition takeBananas(Location x) {
        System.out.print(" takeBannas " + x.name() + "\n");
        return null;
    }

    @SystemAction
    public StateTransition noop(){
        return null;
    }
}
