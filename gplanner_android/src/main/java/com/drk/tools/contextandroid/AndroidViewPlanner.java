package com.drk.tools.contextandroid;

import com.drk.tools.contextandroid.planner.AndroidViewContext;
import com.drk.tools.contextandroid.planner.atoms.MainAtoms;
import com.drk.tools.contextandroid.planner.domain.*;
import com.drk.tools.contextandroid.planner.variables.Bool;
import com.drk.tools.gplannercore.core.Bundle;
import com.drk.tools.gplannercore.core.Plan;
import com.drk.tools.gplannercore.core.search.Searcher;
import com.drk.tools.gplannercore.core.state.State;
import com.drk.tools.gplannercore.planner.PlanStream;
import com.drk.tools.gplannercore.planner.Planner;
import com.drk.tools.gplannercore.planner.search.graphplan.GraphPlan;
import com.drk.tools.gplannercore.planner.search.hsp.HSP;
import com.drk.tools.gplannercore.planner.search.hsp.heuristic.DefaultScore;
import com.drk.tools.gplannercore.planner.state.GStateBuilder;

import java.util.List;

public class AndroidViewPlanner {

    private final AndroidViewInfo androidViewInfo;

    public AndroidViewPlanner(AndroidViewInfo androidViewInfo) {
        this.androidViewInfo = androidViewInfo;
    }

    List<Plan> search(PathTokens pathTokens) {
        Planner planner = new Planner(buildSearcher(), 1);
        State initialState = initialState(pathTokens);
        State finalState = finalState(pathTokens);
        Bundle bundle = buildBundle(pathTokens);
        PlanStream planStream = planner.search(new AndroidViewContext(bundle), initialState, finalState);
        List<Plan> planList = planStream.read();
        planStream.close();
        return planList;
    }

    private Bundle buildBundle(PathTokens pathTokens) {
        Bundle bundle = new Bundle();
        bundle.set(ActionInfo.class.toString(), androidViewInfo.getActionInfo());
        bundle.set(BackInfo.class.toString(), androidViewInfo.getBackInfo());
        bundle.set(HierarchyInfo.class.toString(), androidViewInfo.getHierarchyInfo());
        bundle.set(InitInfo.class.toString(), androidViewInfo.getInitInfo());
        TextInfo textInfo = androidViewInfo.getTextInfo(pathTokens.textToCheck, pathTokens.textToInput);
        bundle.set(TextInfo.class.toString(), textInfo);
        return bundle;

    }

    private State initialState(PathTokens pathTokens) {
        GStateBuilder builder = new GStateBuilder();
        builder.set(MainAtoms.appLaunchPending, Bool.TRUE);
    }

    private State finalState(PathTokens pathTokens) {
        return null;
    }

    private Searcher buildSearcher() {
        return new GraphPlan(new HSP(new DefaultScore()));
    }
}
