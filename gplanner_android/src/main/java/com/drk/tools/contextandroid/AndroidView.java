package com.drk.tools.contextandroid;

import com.drk.tools.contextandroid.domain.*;
import com.drk.tools.contextandroid.variables.*;
import com.drk.tools.gplannercore.annotations.Operator;
import com.drk.tools.gplannercore.core.main.Operators;
import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannercore.planner.state.GStateTransition;

import static com.drk.tools.contextandroid.atoms.MainAtoms.*;

public class AndroidView extends Operators {

    private <T> T get(Class<T> tClass) {
        return obtain(tClass.toString(), tClass);
    }

    private GStateTransition newTransition() {
        GStateTransition stateTransition = new GStateTransition();
        stateTransition.check(isSearchFinished, Bool.FALSE);
        return stateTransition;
    }

    private GStateTransition launchedTransition() {
        GStateTransition stateTransition = newTransition();
        stateTransition.check(isAppLaunched, Bool.TRUE);
        return stateTransition;
    }

    @Operator
    public StateTransition mock(Injection injection, Request request) {
        GStateTransition stateTransition = newTransition();
        stateTransition.check(isAppLaunched, Bool.FALSE);
        stateTransition.check(isMocked, Bool.FALSE);

        stateTransition.set(isMocked, Bool.TRUE);
        stateTransition.not(isMocked, Bool.FALSE);
        return stateTransition;
    }

    @Operator
    public StateTransition launchApp() {
        GStateTransition stateTransition = newTransition();
        stateTransition.check(isAppLaunched, Bool.FALSE);
        stateTransition.check(isMocked, Bool.TRUE);

        stateTransition.set(isAppLaunched, Bool.TRUE);
        stateTransition.not(isAppLaunched, Bool.FALSE);

        InitData initData = get(InitData.class);
        stateTransition.set(at, initData.initScreen);
        return stateTransition;
    }


    @Operator
    public StateTransition checkScreen(Screen screen) {
        GStateTransition stateTransition = launchedTransition();
        stateTransition.check(at, screen);
        stateTransition.set(screenChecked, screen);
        return stateTransition;
    }

    @Operator
    public StateTransition checkVisibility(Element element) {
        GStateTransition stateTransition = launchedTransition();
        HierarchyInfo hierarchyInfo = get(HierarchyInfo.class);
        if (hierarchyInfo.belongsToScreen(element)) {
            stateTransition.check(screenChecked, hierarchyInfo.screenOf(element));
            stateTransition.set(elementVisible, element);
        } else if (hierarchyInfo.belongsToPager(element)) {
            stateTransition.check(hierarchyInfo.pageOf(element), hierarchyInfo.pagerOf(element));
            stateTransition.set(elementVisible, element);
        }
        return stateTransition;
    }

    @Operator
    public StateTransition checkPagerVisibility(PagerElement pagerElement) {
        GStateTransition stateTransition = launchedTransition();
        HierarchyInfo hierarchyInfo = get(HierarchyInfo.class);
        if (hierarchyInfo.belongsToScreen(pagerElement)) {
            stateTransition.check(screenChecked, hierarchyInfo.screenOf(pagerElement));
            stateTransition.set(pagerElementVisible, pagerElement);
        }
        return stateTransition;
    }

    @Operator
    public StateTransition checkElementText(Element element) {
        GStateTransition stateTransition = launchedTransition();
        TextInfo textInfo = get(TextInfo.class);
        if (textInfo.isTextDefined(element)) {
            stateTransition.check(elementVisible, element);
            stateTransition.set(elementTextChecked, element);
        }
        return stateTransition;
    }

    @Operator
    public StateTransition setElementText(Element element) {
        GStateTransition stateTransition = launchedTransition();
        TextInfo textInfo = get(TextInfo.class);
        if (textInfo.isInputTextDefined(element)) {
            stateTransition.check(elementVisible, element);
            stateTransition.set(elementTextSet, element);
        }
        return stateTransition;
    }

    @Operator
    public StateTransition clickElement(Element element) {
        GStateTransition stateTransition = launchedTransition();
        ActionInfo actionInfo = get(ActionInfo.class);
        if (actionInfo.isActionDefined(element)) {
            stateTransition.check(elementVisible, element);
            ActionInfo.ActionData actionData = actionInfo.actionOf(element);
            stateTransition.checkAll(actionData.preconds);
            stateTransition.setAll(actionData.positiveEffects);
            stateTransition.notAll(actionData.negativeEffects);
            stateTransition.set(elementClicked, element);
        }
        return stateTransition;
    }

    @Operator
    public StateTransition backAt(Screen screen) {
        GStateTransition stateTransition = launchedTransition();
        BackInfo backInfo = get(BackInfo.class);
        if (backInfo.isBackInfoDefined(screen)) {
            stateTransition.check(at, screen);
            BackInfo.BackData backData = backInfo.backOf(screen);
            stateTransition.checkAll(backData.preconds);
            stateTransition.setAll(backData.positiveEffects);
            stateTransition.notAll(backData.negativeEffects);
        }
        return stateTransition;
    }

    @Operator
    public StateTransition closeApp() {
        GStateTransition stateTransition = launchedTransition();
        stateTransition.set(isAppLaunched, Bool.FALSE);
        stateTransition.set(isSearchFinished, Bool.TRUE);
        stateTransition.not(isAppLaunched, Bool.TRUE);
        stateTransition.not(isSearchFinished, Bool.FALSE);
        return stateTransition;
    }
}
