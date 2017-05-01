package com.drk.tools.contextandroid.planner.domain;

import com.drk.tools.contextandroid.domain.Action;
import com.drk.tools.contextandroid.domain.AndroidViewInfo;
import com.drk.tools.contextandroid.domain.IntentData;
import com.drk.tools.contextandroid.domain.ViewInfo;
import com.drk.tools.contextandroid.planner.variables.Bool;
import com.drk.tools.contextandroid.planner.variables.Element;
import com.drk.tools.contextandroid.planner.variables.Intent;
import com.drk.tools.contextandroid.planner.variables.Screen;
import com.drk.tools.gplannercore.core.Atom;
import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannercore.core.state.Statement;
import com.drk.tools.gplannercore.planner.state.GStatement;
import com.drk.tools.gplannercore.planner.state.debug.DebugStatement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static com.drk.tools.contextandroid.planner.atoms.MainAtoms.*;

public class ActionInfo {

    private final HashMap<Element, Action> hashActionData;
    private final HashMap<Element, Action> hashImeOptionsActionData;
    private final AndroidViewInfo info;
    private final boolean debug;

    public ActionInfo(HashMap<Element, Action> hashActionData, HashMap<Element, Action> hashImeOptionsActionData, AndroidViewInfo info, boolean debug) {
        this.hashActionData = hashActionData;
        this.hashImeOptionsActionData = hashImeOptionsActionData;
        this.info = info;
        this.debug = debug;
    }

    public boolean isActionDefined(Element element) {
        return hashActionData.containsKey(element);
    }

    public boolean hasImeActionsDefined(Element element) {
        return hashImeOptionsActionData.containsKey(element);
    }

    public void solveImeOptionsAction(Element element, StateTransition stateTransition) {
        Action action = hashImeOptionsActionData.get(element);
        resolve(action, stateTransition);
    }

    public void solveAction(Element element, StateTransition stateTransition) {
        Action action = hashActionData.get(element);
        resolve(action, stateTransition);
    }

    private void resolve(Action action, StateTransition stateTransition) {
        if (action != null) {
            if (action.type == Action.Type.CHANGE_SCREEN) {
                navigationTo(action.screenName, stateTransition);
            } else if (action.type == Action.Type.INTENT) {
                intentTo(action.intentData, stateTransition);
            } else if (action.type == Action.Type.ADD_VIEWS) {
                addViews(action.viewInfos, stateTransition);
            }
        }
    }

    private void addViews(Set<ViewInfo> views, StateTransition stateTransition) {
        for (ViewInfo viewInfo : views) {
            stateTransition.set(buildStatement(addedElement, info.findElementWithViewInfo(viewInfo)));
        }
    }

    private void navigationTo(String screenName, StateTransition stateTransition) {
        Screen screen = info.findScreenByName(screenName);
        Set<Statement> positiveEffects = new HashSet<>();
        Set<Statement> negativeEffects = new HashSet<>();
        positiveEffects.add(buildStatement(navigateTo, screen));
        positiveEffects.add(buildStatement(screenNavigationPending, Bool.TRUE));
        negativeEffects.add(buildStatement(screenNavigationPending, Bool.FALSE));
        stateTransition.setAll(positiveEffects);
        stateTransition.notAll(negativeEffects);
    }

    private void intentTo(IntentData intentData, StateTransition stateTransition) {
        Intent intent = info.findIntentByName(intentData);
        Set<Statement> positiveEffects = new HashSet<>();
        Set<Statement> negativeEffects = new HashSet<>();
        positiveEffects.add(buildStatement(intentTo, intent));
        positiveEffects.add(buildStatement(launchIntentPending, Bool.TRUE));
        negativeEffects.add(buildStatement(launchIntentPending, Bool.FALSE));
        stateTransition.setAll(positiveEffects);
        stateTransition.notAll(negativeEffects);
    }


    private <A extends Atom<E>, E extends Enum> Statement buildStatement(A atom, E variable) {
        if (debug) {
            return DebugStatement.from(atom, variable);
        }
        return GStatement.from(atom, variable);
    }

}
