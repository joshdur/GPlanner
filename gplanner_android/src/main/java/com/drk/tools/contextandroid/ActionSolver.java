package com.drk.tools.contextandroid;

import com.drk.tools.contextandroid.domain.NavigationInfo;
import com.drk.tools.contextandroid.domain.ScreenInfo;
import com.drk.tools.contextandroid.planner.domain.ActionInfo;
import com.drk.tools.contextandroid.planner.variables.Bool;
import com.drk.tools.contextandroid.planner.variables.Screen;
import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.planner.state.GStatement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.drk.tools.contextandroid.planner.atoms.MainAtoms.navigateTo;
import static com.drk.tools.contextandroid.planner.atoms.MainAtoms.screenNavigationPending;

class ActionSolver {

    private final HashMap<Screen, ScreenInfo> mapScreens;

    ActionSolver(HashMap<Screen, ScreenInfo> mapScreens) {
        this.mapScreens = mapScreens;
    }

    ActionInfo.ActionData solveActionData(NavigationInfo navigationInfo) {
        if (navigationInfo.type == NavigationInfo.Type.CHANGE_SCREEN) {
            String screenName = navigationInfo.screenName;
            return navigationTo(screenName);
        }
        throw new IllegalStateException("Not recognized click type " + navigationInfo.type.name());
    }

    Screen solveBackData(NavigationInfo navigationInfo) {
        String screenName = navigationInfo.screenName;
        return findByName(screenName);
    }

    private ActionInfo.ActionData navigationTo(String screenName) {
        Screen screen = findByName(screenName);
        Set<Statement> preconds = new HashSet<>();
        Set<Statement> positiveEffects = new HashSet<>();
        Set<Statement> negativeEffects = new HashSet<>();
        positiveEffects.add(GStatement.from(navigateTo, screen));
        positiveEffects.add(GStatement.from(screenNavigationPending, Bool.TRUE));
        return new ActionInfo.ActionData(preconds, positiveEffects, negativeEffects);
    }

    private Screen findByName(String name) {
        for (Map.Entry<Screen, ScreenInfo> entry : mapScreens.entrySet()) {
            if (entry.getValue().name.equalsIgnoreCase(name)) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException("Not found screenInfo for " + name);
    }
}
