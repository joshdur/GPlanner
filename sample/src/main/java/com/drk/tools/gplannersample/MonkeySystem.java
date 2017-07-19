package com.drk.tools.gplannersample;

import com.drk.tools.gplannercore.annotations.SystemAction;
import com.drk.tools.gplannercore.core.main.SystemActions;
import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannersample.Monkey.*;
import com.drk.tools.gplannersample.vars.Location;

public class MonkeySystem extends SystemActions {

    @SystemAction
    public StateTransition climbDown(Location x) {
        System.out.print(" climbdown " + x.toString() );
        return null;
    }

    @SystemAction
    public StateTransition climbUp(Location x) {
        System.out.print(" climbUp " + x.toString() );
        return null;
    }

    @SystemAction
    public StateTransition moveBox(Location x, Location y) {
        System.out.print(" moveBox " + x.toString() + " " + y.toString());
        return null;
    }

    @SystemAction
    public StateTransition move(Location x, Location y) {
        System.out.print(" move " + x.toString() + " " + y.toString());
        return null;
    }

    @SystemAction
    public StateTransition takeBananas(Location x) {
        System.out.print(" takeBannas " + x.toString() + "\n");
        return null;
    }

    @SystemAction
    public StateTransition noop(){
        return null;
    }
}
