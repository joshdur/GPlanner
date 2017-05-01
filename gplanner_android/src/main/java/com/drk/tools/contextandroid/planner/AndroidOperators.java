package com.drk.tools.contextandroid.planner;

import com.drk.tools.contextandroid.domain.AndroidViewInfo;
import com.drk.tools.contextandroid.domain.ElementInputText;
import com.drk.tools.contextandroid.planner.domain.*;
import com.drk.tools.contextandroid.planner.variables.*;
import com.drk.tools.gplannercore.annotations.Operator;
import com.drk.tools.gplannercore.core.main.Operators;
import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannercore.planner.state.GStateTransition;
import com.drk.tools.gplannercore.planner.state.debug.DebugStateTransition;

import static com.drk.tools.contextandroid.planner.atoms.MainAtoms.*;

public class AndroidOperators extends Operators {

    private <T> T get(Class<T> tClass) {
        return obtain(tClass.toString(), tClass);
    }

    private StateTransition newTransition() {
        SearchInfo searchInfo = get(SearchInfo.class);
        return searchInfo.isDebug ? new DebugStateTransition() : new GStateTransition();
    }

    @Operator
    public StateTransition mock(Mock mock) {
        StateTransition stateTransition = newTransition();
        stateTransition.check(isSearchFinished, Bool.FALSE);
        stateTransition.check(launchPending, Bool.TRUE);
        stateTransition.check(mockPending, Bool.TRUE);
        AndroidViewInfo info = get(AndroidViewInfo.class);
        if (info.mapMocks.containsKey(mock)) {
            stateTransition.set(mocked, mock);
            stateTransition.set(mockPending, Bool.FALSE);
            stateTransition.not(mockPending, Bool.TRUE);
        }
        return stateTransition;
    }

    @Operator
    public StateTransition launchApp() {
        StateTransition stateTransition = newTransition();
        stateTransition.check(isSearchFinished, Bool.FALSE);
        stateTransition.check(launchPending, Bool.TRUE);
        stateTransition.check(mockPending, Bool.FALSE);

        stateTransition.set(launchPending, Bool.FALSE);
        stateTransition.not(launchPending, Bool.TRUE);

        InitInfo initInfo = get(InitInfo.class);
        if (initInfo.hasInitScreen()) {
            stateTransition.set(at, initInfo.getFirstScreen());
        }
        return stateTransition;
    }

    @Operator
    public StateTransition navigate(Screen from, Screen to) {
        StateTransition stateTransition = newTransition();
        withAppLaunched(stateTransition);
        stateTransition.check(launchIntentPending, Bool.FALSE);
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
    public StateTransition checkIntent(Intent intent) {
        StateTransition stateTransition = newTransition();
        withAppLaunched(stateTransition);
        stateTransition.check(launchIntentPending, Bool.TRUE);
        stateTransition.check(intentTo, intent);

        stateTransition.set(launchIntentPending, Bool.FALSE);
        stateTransition.set(intentChecked, intent);

        stateTransition.not(intentTo, intent);
        stateTransition.not(launchIntentPending, Bool.TRUE);
        return stateTransition;
    }

    @Operator
    public StateTransition checkScreen(Screen screen) {
        StateTransition stateTransition = newTransition();
        withAppLaunched(stateTransition);
        noPendings(stateTransition);

        stateTransition.check(at, screen);
        stateTransition.set(screenChecked, screen);
        return stateTransition;
    }

    @Operator
    public StateTransition checkVisibility(Element element) {
        StateTransition stateTransition = newTransition();
        AndroidViewInfo info = get(AndroidViewInfo.class);
        if (info.isDefined(element)) {
            withAppLaunched(stateTransition);
            noPendings(stateTransition);
            checkAtScreen(stateTransition, element);
            stateTransition.set(elementVisible, element);
        }
        return stateTransition;
    }

    @Operator
    public StateTransition checkElementState(Element element) {
        StateTransition stateTransition = newTransition();
        AndroidViewInfo info = get(AndroidViewInfo.class);
        if (info.isDefined(element)) {
            withAppLaunched(stateTransition);
            noPendings(stateTransition);
            checkAtScreen(stateTransition, element);
            stateTransition.set(elementStateChecked, element);
        }
        return stateTransition;
    }

    @Operator
    public StateTransition checkPagerVisibility(PagerElement pagerElement) {
        StateTransition stateTransition = newTransition();
        AndroidViewInfo info = get(AndroidViewInfo.class);
        if (info.isDefined(pagerElement)) {
            withAppLaunched(stateTransition);
            noPendings(stateTransition);
            checkAtScreen(stateTransition, pagerElement);
            stateTransition.set(pagerElementVisible, pagerElement);
        }
        return stateTransition;
    }


    @Operator
    public StateTransition checkElementText(Element element) {
        StateTransition stateTransition = newTransition();
        withAppLaunched(stateTransition);
        noPendings(stateTransition);

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
        StateTransition stateTransition = newTransition();
        withAppLaunched(stateTransition);
        noPendings(stateTransition);
        checkAtScreen(stateTransition, element);
        TextInfo textInfo = get(TextInfo.class);
        ActionInfo actionInfo = get(ActionInfo.class);
        if (textInfo.isInputTextDefined(element)) {
            ElementInputText inputText = textInfo.getInputText(element);
            stateTransition.check(elementVisible, element);
            stateTransition.set(elementTextSet, element);
            if (inputText.pressImeActionButton && actionInfo.hasImeActionsDefined(element)) {
                actionInfo.solveImeOptionsAction(element, stateTransition);
            }
        }
        return stateTransition;
    }

    @Operator
    public StateTransition clickElement(Element element) {
        StateTransition stateTransition = newTransition();
        withAppLaunched(stateTransition);
        noPendings(stateTransition);

        checkAtScreen(stateTransition, element);
        ActionInfo actionInfo = get(ActionInfo.class);
        if (actionInfo.isActionDefined(element)) {
            stateTransition.check(elementVisible, element);
            actionInfo.solveAction(element, stateTransition);
            stateTransition.set(elementClicked, element);
        }
        return stateTransition;
    }

    @Operator
    public StateTransition backAt(Screen screen) {
        StateTransition stateTransition = newTransition();
        withAppLaunched(stateTransition);
        noPendings(stateTransition);

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
        StateTransition stateTransition = newTransition();
        withAppLaunched(stateTransition);
        noPendings(stateTransition);

        stateTransition.set(launchPending, Bool.FALSE);
        stateTransition.set(isSearchFinished, Bool.TRUE);
        stateTransition.not(launchPending, Bool.TRUE);
        stateTransition.not(isSearchFinished, Bool.FALSE);
        return stateTransition;
    }


    private void withAppLaunched(StateTransition stateTransition) {
        stateTransition.check(isSearchFinished, Bool.FALSE);
        stateTransition.check(launchPending, Bool.FALSE);
    }

    private void noPendings(StateTransition stateTransition) {
        stateTransition.check(screenNavigationPending, Bool.FALSE);
        stateTransition.check(launchIntentPending, Bool.FALSE);
    }

    private void checkAtScreen(StateTransition stateTransition, Element element) {
        AndroidViewInfo info = get(AndroidViewInfo.class);
        if(!info.isPresent(element)){
            stateTransition.check(addedElement, element);
        }
        HierarchyInfo hierarchyInfo = get(HierarchyInfo.class);
        if (hierarchyInfo.belongsToScreen(element)) {
            Screen screen = hierarchyInfo.screenOf(element);
            stateTransition.check(screenChecked, screen);
            stateTransition.check(at, screen);
        } else if (hierarchyInfo.belongsToPager(element)) {
            PagerElement pagerElement = hierarchyInfo.pagerOf(element);
            checkAtScreen(stateTransition, pagerElement);
        }
    }

    private void checkAtScreen(StateTransition stateTransition, PagerElement element) {
        HierarchyInfo hierarchyInfo = get(HierarchyInfo.class);
        if (hierarchyInfo.belongsToScreen(element)) {
            Screen screen = hierarchyInfo.screenOf(element);
            stateTransition.check(screenChecked, screen);
            stateTransition.check(at, screen);
        }
    }
}
