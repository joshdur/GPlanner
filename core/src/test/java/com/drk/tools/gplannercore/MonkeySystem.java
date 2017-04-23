package com.drk.tools.gplannercore;

import com.drk.tools.gplannercore.Monkey.Location;
import com.drk.tools.gplannercore.annotations.SystemAction;
import com.drk.tools.gplannercore.core.main.SystemActions;
import com.drk.tools.gplannercore.core.state.StateTransition;


public class MonkeySystem extends SystemActions {

    @SystemAction
    public StateTransition climbDown(Location x) {
        System.out.print("climbdown");
        return null;
    }

    @SystemAction
    public StateTransition climbUp(Location x) {
        System.out.print("climbUp");
        return null;
    }

    @SystemAction
    public StateTransition move(Location x, Location y) {
        System.out.print("move");
        return null;
    }

    @SystemAction
    public StateTransition takeBananas(Location x) {
        System.out.print("takeBannas");
        return null;
    }

    @SystemAction
    public StateTransition noop(){
        return null;
    }
}
