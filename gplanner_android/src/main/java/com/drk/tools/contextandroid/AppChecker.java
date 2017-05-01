package com.drk.tools.contextandroid;

import com.drk.tools.contextandroid.domain.*;
import com.drk.tools.contextandroid.planner.AndroidOperatorsContext;
import com.drk.tools.contextandroid.planner.domain.*;
import com.drk.tools.contextandroid.planner.variables.Bool;
import com.drk.tools.gplannercore.core.Bundle;
import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.Plan;
import com.drk.tools.gplannercore.core.search.Searcher;
import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.state.StateBuilder;
import com.drk.tools.gplannercore.planner.PlanStream;
import com.drk.tools.gplannercore.planner.Planner;
import com.drk.tools.gplannercore.planner.search.forward.SimpleForward;
import com.drk.tools.gplannercore.planner.search.graphplan.GraphPlan;
import com.drk.tools.gplannercore.planner.search.hsp.HSP;
import com.drk.tools.gplannercore.planner.search.hsp.heuristic.GraphPlanScore;
import com.drk.tools.gplannercore.planner.state.GStateBuilder;
import com.drk.tools.gplannercore.planner.state.debug.DebugStateBuilder;

import java.util.List;

import static com.drk.tools.contextandroid.planner.atoms.MainAtoms.*;

public class AppChecker {

    private final AndroidViewInfo androidViewInfo;
    private final AndroidSystem androidSystem;
    private final boolean debug;
    private final InfoBuilder infoBuilder;

    public AppChecker(AndroidViewInfo androidViewInfo, AndroidSystem androidSystem, boolean debug) {
        this.androidViewInfo = androidViewInfo;
        this.androidSystem = androidSystem;
        this.debug = debug;
        this.infoBuilder = new InfoBuilder(androidViewInfo, debug);
    }

    public AppChecker(AndroidViewInfo androidViewInfo, AndroidSystem androidSystem) {
        this(androidViewInfo, androidSystem, false);
    }

    public void assertScenario(Scenario scenario, int nPaths) throws Throwable {
        State initState = initialState(scenario);
        State finalState = finalState(scenario);
        Bundle bundle = buildBundle(scenario);
        Context context = new AndroidOperatorsContext(bundle);
        List<Plan> paths = search(context, initState, finalState, nPaths);
        if (debug) {
            asString(paths, context);
        }
        runPaths(context, paths);
    }

    private void runPaths(Context context, List<Plan> paths) throws Throwable {
        if(paths.isEmpty()){
            throw new IllegalStateException("There is no paths for defined scenario");
        }
        for (Plan plan : paths) {
            context.execute(plan);
        }
    }

    private List<Plan> search(Context context, State initState, State finalState, int nPaths) {
        Planner planner = new Planner(buildSearcher(), nPaths);
        PlanStream planStream = planner.search(context, initState, finalState);
        List<Plan> planList = planStream.read();
        planStream.close();
        return planList;
    }

    private void asString(List<Plan> plans, Context context) {
        for (Plan plan : plans) {
            String strPlan = context.asString(plan);
            System.out.println(strPlan);
        }
    }

    private Bundle buildBundle(Scenario scenario) {
        Bundle bundle = new Bundle();
        bundle.set(AndroidSystem.class.toString(), androidSystem);
        bundle.set(AndroidViewInfo.class.toString(), androidViewInfo);
        bundle.set(ActionInfo.class.toString(), infoBuilder.getActionInfo());
        bundle.set(BackInfo.class.toString(), infoBuilder.getBackInfo());
        bundle.set(HierarchyInfo.class.toString(), infoBuilder.getHierarchyInfo());
        bundle.set(InitInfo.class.toString(), infoBuilder.getInitInfo());
        bundle.set(TextInfo.class.toString(), infoBuilder.getTextInfo(scenario));
        bundle.set(ElementStateInfo.class.toString(), infoBuilder.getElementStateInfo(scenario));
        bundle.set(SearchInfo.class.toString(), new SearchInfo(debug));
        return bundle;
    }

    private State initialState(Scenario scenario) {
        StateBuilder builder = stateBuilder();
        builder.set(launchPending, Bool.TRUE);
        builder.set(mockPending, scenario.shouldMock() ? Bool.TRUE : Bool.FALSE);
        builder.set(isSearchFinished, Bool.FALSE);
        builder.set(screenNavigationPending, Bool.FALSE);
        builder.set(launchIntentPending, Bool.FALSE);
        return builder.build();
    }

    private State finalState(Scenario scenario) {
        StateBuilder builder = stateBuilder();
        for (ElementText elementText : scenario.textToCheck) {
            builder.set(elementTextChecked, androidViewInfo.findElementWithViewInfo(elementText.viewInfo));
        }
        for (ElementInputText elementText : scenario.textToInput) {
            builder.set(elementTextSet, androidViewInfo.findElementWithViewInfo(elementText.viewInfo));
        }
        for (String screenName : scenario.ats) {
            builder.set(screenChecked, androidViewInfo.findScreenByName(screenName));
        }
        for (ViewInfo info : scenario.clickeds) {
            builder.set(elementClicked, androidViewInfo.findElementWithViewInfo(info));
        }
        for(ElementState elementState : scenario.elementStates) {
            builder.set(elementStateChecked, androidViewInfo.findElementWithViewInfo(elementState.info));
        }
        if (scenario.shouldMock()) {
            builder.set(mocked, androidViewInfo.findMockByEnum(scenario.mock));
        }
        builder.set(isSearchFinished, Bool.TRUE);
        builder.set(launchIntentPending, Bool.FALSE);
        return builder.build();
    }

    private StateBuilder stateBuilder() {
        return debug ? new DebugStateBuilder() : new GStateBuilder();
    }

    private Searcher buildSearcher() {
       // return new SimpleForward(true);
        return new GraphPlan(new HSP(new GraphPlanScore()));
    }
}
