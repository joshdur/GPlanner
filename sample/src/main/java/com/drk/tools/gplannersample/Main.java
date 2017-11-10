package com.drk.tools.gplannersample;

import com.drk.tools.gplannercore.core.Plan;
import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.planner.PlanStream;
import com.drk.tools.gplannercore.planner.Planner;
import com.drk.tools.gplannercore.planner.search.forward.SimpleForward;
import com.drk.tools.gplannercore.planner.search.graphplan.GraphPlan;
import com.drk.tools.gplannercore.planner.state.GStateBuilder;
import com.drk.tools.gplannersample.vars.LevelStateRange;
import com.drk.tools.gplannersample.vars.LocationRange;
import com.drk.tools.gplannersample.vars.ThingRange;

import java.util.List;

import static com.drk.tools.gplannersample.Monkey.*;


public class Main {

    private static boolean debug = true;

    public static void main(String[] args) throws Throwable {
        //Planner planner = new Planner(new GraphPlan(new SimpleForward()), 10);
        Planner planner = new Planner(new SimpleForward(), 10);
        DefaultContext monkeyContext = new DefaultContext(debug);
        PlanStream planStream = planner.search(monkeyContext, initialState(), finalState());
        List<Plan> plans = planStream.read();
        while (!plans.isEmpty()) {
            printPlans(plans, monkeyContext);
            plans = planStream.read();
        }

    }

    private static void printPlans(List<Plan> plans, DefaultContext context) throws Throwable {
        for (Plan plan : plans) {
            String planStr = context.asString(plan);
            System.out.println(planStr);
            context.execute(plan);
        }
    }


    private static State initialState() {
        GStateBuilder stateBuilder = new GStateBuilder(debug);
        stateBuilder.set(at, LocationRange.A);
        stateBuilder.set(boxAt, LocationRange.C);
        stateBuilder.set(bananasAt, LocationRange.B);
        stateBuilder.set(level, LevelStateRange.LOW);
        return stateBuilder.build();
    }

    private static State finalState() {
        GStateBuilder stateBuilder = new GStateBuilder(debug);
        stateBuilder.set(have, ThingRange.BANANAS);
        stateBuilder.not(test);
        return stateBuilder.build();
    }
}
