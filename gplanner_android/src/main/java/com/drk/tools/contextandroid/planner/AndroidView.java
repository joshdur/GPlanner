package com.drk.tools.contextandroid.planner;

import com.drk.tools.contextandroid.planner.domain.*;
import com.drk.tools.contextandroid.planner.variables.*;
import com.drk.tools.gplannercore.annotations.Operator;
import com.drk.tools.gplannercore.core.main.Operators;
import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannercore.planner.state.GStateTransition;

import static com.drk.tools.contextandroid.planner.atoms.MainAtoms.*;

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
        stateTransition.check(appLaunchPending, Bool.FALSE);
        return stateTransition;
    }

    @Operator
    public StateTransition mockInjection(Injection injection) {
        GStateTransition stateTransition = newTransition();
        stateTransition.check(appLaunchPending, Bool.TRUE);
        stateTransition.check(mockInjectionPending, Bool.TRUE);

        stateTransition.set(mockInjectionPending, Bool.FALSE);
        stateTransition.not(mockInjectionPending, Bool.TRUE);
        return stateTransition;
    }

    @Operator
    public StateTransition mockRequest(Request request) {
        GStateTransition stateTransition = newTransition();
        stateTransition.check(appLaunchPending, Bool.TRUE);
        stateTransition.check(mockInjectionPending, Bool.FALSE);
        stateTransition.check(mockRequestPending, Bool.TRUE);

        stateTransition.set(mockRequestPending, Bool.FALSE);
        stateTransition.not(mockRequestPending, Bool.TRUE);
        return stateTransition;
    }



    @Operator
    public StateTransition launchApp() {
        GStateTransition stateTransition = newTransition();
        stateTransition.check(appLaunchPending, Bool.TRUE);
        stateTransition.check(mockInjectionPending, Bool.FALSE);
        stateTransition.check(mockRequestPending, Bool.FALSE);

        stateTransition.set(appLaunchPending, Bool.FALSE);
        stateTransition.not(appLaunchPending, Bool.TRUE);

        InitInfo initInfo = get(InitInfo.class);
        if (initInfo.hasInitScreen()) {
            stateTransition.set(at, initInfo.getFirstScreen());
        }
        return stateTransition;
    }

    @Operator
    public StateTransition navigate(Screen from, Screen to) {
        GStateTransition stateTransition = launchedTransition();
        if (from == to) {
            return stateTransition;
        }
        stateTransition.check(screenNavigationPending, Bool.TRUE);
        stateTransition.check(at, from);
        stateTransition.check(navigateTo, to);

        stateTransition.set(at, to);
        stateTransition.set(screenNavigationPending, Bool.FALSE);

        stateTransition.not(at, from);
        stateTransition.not(navigateTo, to);
        stateTransition.not(screenNavigationPending, Bool.TRUE);
        return stateTransition;
    }

    @Operator
    public StateTransition checkScreen(Screen screen) {
        GStateTransition stateTransition = launchedTransition();
        stateTransition.check(screenNavigationPending, Bool.FALSE);
        stateTransition.check(at, screen);
        stateTransition.set(screenChecked, screen);
        return stateTransition;
    }

    @Operator
    public StateTransition checkVisibility(Element element) {
        GStateTransition stateTransition = launchedTransition();
        stateTransition.check(screenNavigationPending, Bool.FALSE);
        checkAtScreen(stateTransition, element);
        stateTransition.set(elementVisible, element);
        return stateTransition;
    }

    @Operator
    public StateTransition checkPagerVisibility(PagerElement pagerElement) {
        GStateTransition stateTransition = launchedTransition();
        stateTransition.check(screenNavigationPending, Bool.FALSE);
        checkAtScreen(stateTransition, pagerElement);
        stateTransition.set(pagerElementVisible, pagerElement);
        return stateTransition;
    }


    @Operator
    public StateTransition checkElementText(Element element) {
        GStateTransition stateTransition = launchedTransition();
        stateTransition.check(screenNavigationPending, Bool.FALSE);
        checkAtScreen(stateTransition, element);
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
        stateTransition.check(screenNavigationPending, Bool.FALSE);
        checkAtScreen(stateTransition, element);
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
        stateTransition.check(screenNavigationPending, Bool.FALSE);
        checkAtScreen(stateTransition, element);
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
        stateTransition.check(screenNavigationPending, Bool.FALSE);
        stateTransition.check(at, screen);
        BackInfo backInfo = get(BackInfo.class);
        if (backInfo.isBackInfoDefined(screen)) {
            stateTransition.set(at, backInfo.backOf(screen));
            stateTransition.set(backAt, screen);
            stateTransition.not(at, screen);
        }
        return stateTransition;
    }

    @Operator
    public StateTransition closeApp() {
        GStateTransition stateTransition = launchedTransition();
        stateTransition.check(screenNavigationPending, Bool.FALSE);
        stateTransition.set(appLaunchPending, Bool.FALSE);
        stateTransition.set(isSearchFinished, Bool.TRUE);
        stateTransition.not(appLaunchPending, Bool.TRUE);
        stateTransition.not(isSearchFinished, Bool.FALSE);
        return stateTransition;
    }

    private void checkAtScreen(GStateTransition stateTransition, Element element) {
        HierarchyInfo hierarchyInfo = get(HierarchyInfo.class);
        if (hierarchyInfo.belongsToScreen(element)) {
            Screen screen = hierarchyInfo.screenOf(element);
            stateTransition.check(screenChecked, screen);
            stateTransition.check(at, screen);
        } else {
            PagerElement pagerElement = hierarchyInfo.pagerOf(element);
            checkAtScreen(stateTransition, pagerElement);
        }
    }

    private void checkAtScreen(GStateTransition stateTransition, PagerElement element) {
        HierarchyInfo hierarchyInfo = get(HierarchyInfo.class);
        Screen screen = hierarchyInfo.screenOf(element);
        stateTransition.check(screenChecked, screen);
        stateTransition.check(at, screen);
    }
}
