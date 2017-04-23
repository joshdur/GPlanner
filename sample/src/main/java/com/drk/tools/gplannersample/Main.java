package com.drk.tools.gplannersample;

import com.drk.tools.gplannercore.core.Plan;
import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.streams.GStreamException;
import com.drk.tools.gplannercore.planner.PlanStream;
import com.drk.tools.gplannercore.planner.Planner;
import com.drk.tools.gplannercore.planner.search.forward.SimpleForward;
import com.drk.tools.gplannercore.planner.search.graphplan.GraphPlan;
import com.drk.tools.gplannercore.planner.state.GStateBuilder;

import java.util.List;

import static com.drk.tools.gplannersample.Monkey.*;
import static com.drk.tools.gplannersample.Monkey.Location.*;


public class Main {

    public static void main(String[] args) throws Throwable {
        Planner planner = new Planner(new GraphPlan(new SimpleForward()), 10);
        MonkeyContext monkeyContext = new MonkeyContext();
        PlanStream planStream = planner.search(monkeyContext, initialState(), finalState());
        List<Plan> plans = planStream.read();
        while (!plans.isEmpty()) {
            printPlans(plans, monkeyContext);
            plans = planStream.read();
        }

    }

    private static void printPlans(List<Plan> plans, MonkeyContext context) throws Throwable {
        for (Plan plan : plans) {
            String planStr = context.asString(plan);
            System.out.println(planStr);
            //context.execute(plan);
        }
    }


    private static State initialState() {
        GStateBuilder stateBuilder = new GStateBuilder();
        stateBuilder.set(at, A);
        stateBuilder.set(boxAt, C);
        stateBuilder.set(bananasAt, B);
        stateBuilder.set(level, LevelState.LOW);
        return stateBuilder.build();
    }

    private static State finalState() {
        GStateBuilder stateBuilder = new GStateBuilder();
        stateBuilder.set(have, Thing.BANANAS);
        return stateBuilder.build();
    }
}
