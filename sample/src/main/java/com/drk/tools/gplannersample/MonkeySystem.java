package com.drk.tools.gplannersample;

import com.drk.tools.gplannercore.annotations.SystemAction;
import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.main.BaseOperators;
import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannersample.vars.Location;

public class MonkeySystem extends BaseOperators {

    public MonkeySystem(Context context) {
        super(context);
    }

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
