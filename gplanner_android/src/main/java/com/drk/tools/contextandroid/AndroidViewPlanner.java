package com.drk.tools.contextandroid;

import com.drk.tools.contextandroid.planner.AndroidViewContext;
import com.drk.tools.contextandroid.planner.domain.*;
import com.drk.tools.contextandroid.planner.variables.Bool;
import com.drk.tools.gplannercore.core.Bundle;
import com.drk.tools.gplannercore.core.Plan;
import com.drk.tools.gplannercore.core.search.Searcher;
import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.core.state.StateBuilder;
import com.drk.tools.gplannercore.planner.PlanStream;
import com.drk.tools.gplannercore.planner.Planner;
import com.drk.tools.gplannercore.planner.search.hsp.HSP;
import com.drk.tools.gplannercore.planner.search.hsp.heuristic.GraphPlanScore;
import com.drk.tools.gplannercore.planner.state.GStateBuilder;
import com.drk.tools.gplannercore.planner.state.debug.DebugStateBuilder;

import java.util.List;

import static com.drk.tools.contextandroid.planner.atoms.MainAtoms.*;

public class AndroidViewPlanner {

    private final AndroidViewInfo androidViewInfo;
    private final boolean debug;

    public AndroidViewPlanner(AndroidViewInfo androidViewInfo, boolean debug) {
        this.androidViewInfo = androidViewInfo;
        this.debug = debug;
    }

    public AndroidViewPlanner(AndroidViewInfo androidViewInfo) {
        this(androidViewInfo, false);
    }

    List<Plan> search(PathTokens pathTokens) {
        Planner planner = new Planner(buildSearcher(), 1);
        State initialState = initialState(pathTokens);
        State finalState = finalState(pathTokens);
        Bundle bundle = buildBundle(pathTokens);
        AndroidViewContext context = new AndroidViewContext(bundle);
        PlanStream planStream = planner.search(context, initialState, finalState);
        List<Plan> planList = planStream.read();
        planStream.close();
        asString(planList, context);
        return planList;
    }

    private void asString(List<Plan> plans, AndroidViewContext context) {
        for (Plan plan : plans) {
            String strPlan = context.asString(plan);
            System.out.println(strPlan);
        }
    }

    private Bundle buildBundle(PathTokens pathTokens) {
        Bundle bundle = new Bundle();
        bundle.set(ActionInfo.class.toString(), InfoBuilder.getActionInfo(androidViewInfo));
        bundle.set(BackInfo.class.toString(), InfoBuilder.getBackInfo(androidViewInfo));
        bundle.set(HierarchyInfo.class.toString(), InfoBuilder.getHierarchyInfo(androidViewInfo));
        bundle.set(InitInfo.class.toString(), InfoBuilder.getInitInfo(androidViewInfo));
        bundle.set(TextInfo.class.toString(), InfoBuilder.getTextInfo(androidViewInfo, pathTokens));
        bundle.set(SearchInfo.class.toString(), new SearchInfo(debug));
        return bundle;
    }

    private State initialState(PathTokens pathTokens) {
        StateBuilder builder = stateBuilder();
        builder.set(launchPending, Bool.TRUE);
        builder.set(mockPending, pathTokens.shouldMock() ? Bool.TRUE : Bool.FALSE);
        builder.set(isSearchFinished, Bool.FALSE);
        builder.set(screenNavigationPending, Bool.FALSE);
        return builder.build();
    }

    private State finalState(PathTokens pathTokens) {
        StateBuilder builder = stateBuilder();
        for (IdText idText : pathTokens.textToCheck) {
            builder.set(elementTextChecked, androidViewInfo.findElementWithId(idText.resId));
        }
        for (IdText idText : pathTokens.textToInput) {
            builder.set(elementTextSet, androidViewInfo.findElementWithId(idText.resId));
        }
        for (String screenName : pathTokens.ats) {
            builder.set(screenChecked, androidViewInfo.findScreenByName(screenName));
        }
        for (int resId : pathTokens.clickeds) {
            builder.set(elementClicked, androidViewInfo.findElementWithId(resId));
        }
        if (pathTokens.shouldMock()) {
            builder.set(mocked, androidViewInfo.findMockByEnum(pathTokens.mock));
        }
        return builder.build();
    }

    private StateBuilder stateBuilder() {
        return debug ? new DebugStateBuilder() : new GStateBuilder();
    }

    private Searcher buildSearcher() {
        return new HSP(new GraphPlanScore());
    }
}
