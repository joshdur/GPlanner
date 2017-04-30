package com.drk.tools.contextandroid.planner;

import com.drk.tools.contextandroid.AndroidSystem;
import com.drk.tools.contextandroid.domain.*;
import com.drk.tools.contextandroid.planner.domain.ElementStateInfo;
import com.drk.tools.contextandroid.planner.domain.InitInfo;
import com.drk.tools.contextandroid.planner.domain.SearchInfo;
import com.drk.tools.contextandroid.planner.domain.TextInfo;
import com.drk.tools.contextandroid.planner.variables.*;
import com.drk.tools.gplannercore.annotations.SystemAction;
import com.drk.tools.gplannercore.core.main.SystemActions;
import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannercore.planner.state.GStateTransition;
import com.drk.tools.gplannercore.planner.state.debug.DebugStateTransition;

import java.util.ArrayList;
import java.util.List;

public class AndroidSystemActions extends SystemActions {

    private StringBuilder planStr = new StringBuilder();
    private List<Runnable> runnables = new ArrayList<>();

    @SystemAction
    public StateTransition mock(Mock mock) {
        final Enum value = get(AndroidViewInfo.class).mapMocks.get(mock);
        planStr.append(String.format("mock %s | ", value.name()));
        runnables.add(new Runnable() {
            @Override
            public void run() {
                system().mock(value);
            }
        });

        return newTransition();
    }

    @SystemAction
    public StateTransition launchApp() {
        InitInfo initInfo = get(InitInfo.class);
        ScreenInfo screenInfo = null;
        String screenName = "";
        if (initInfo.hasInitScreen()) {
            AndroidViewInfo androidViewInfo = get(AndroidViewInfo.class);
            screenInfo = androidViewInfo.mapScreens.get(initInfo.getFirstScreen());
            screenName = screenInfo.name;
        }
        planStr.append(String.format("launchApp %s |", screenName));
        final ScreenInfo startScreen = screenInfo;
        runnables.add(new Runnable() {
            @Override
            public void run() {
                system().launchApp(startScreen);
            }
        });
        return newTransition();
    }

    @SystemAction
    public StateTransition checkIntent(Intent intent) {
        AndroidViewInfo androidViewInfo = get(AndroidViewInfo.class);
        final IntentData intentData = androidViewInfo.mapIntents.get(intent);
        planStr.append(String.format("checkIntent %s |", intentData.toString()));
        runnables.add(new Runnable() {
            @Override
            public void run() {
                system().checkIntent(intentData);
            }
        });
        return newTransition();
    }

    @SystemAction
    public StateTransition checkScreen(Screen screen) {
        AndroidViewInfo androidViewInfo = get(AndroidViewInfo.class);
        final ScreenInfo screenInfo = androidViewInfo.mapScreens.get(screen);
        planStr.append(String.format("checkScreen %s |", screenInfo.name));
        runnables.add(new Runnable() {
            @Override
            public void run() {
                system().checkScreen(screenInfo);
            }
        });
        return newTransition();
    }

    @SystemAction
    public StateTransition checkVisibility(Element element) {
        AndroidViewInfo androidViewInfo = get(AndroidViewInfo.class);
        final ViewInfo viewInfo = androidViewInfo.mapElements.get(element);
        planStr.append(String.format("checkVisibility %d |", viewInfo.id));
        runnables.add(new Runnable() {
            @Override
            public void run() {
                system().checkVisibility(viewInfo);
            }
        });
        return newTransition();
    }

    @SystemAction
    public StateTransition checkElementState(Element element) {
        ElementStateInfo elementStateInfo = get(ElementStateInfo.class);
        AndroidViewInfo androidViewInfo = get(AndroidViewInfo.class);
        final ViewInfo viewInfo = androidViewInfo.mapElements.get(element);
        final ElementState elementState = elementStateInfo.getElementState(element);
        planStr.append(String.format("checkElementState %s |", elementState.info.toString()));
        runnables.add(new Runnable() {
            @Override
            public void run() {
                system().checkElementState(viewInfo, elementState);
            }
        });
        return newTransition();
    }

    @SystemAction
    public StateTransition checkPagerVisibility(PagerElement pagerElement) {
        AndroidViewInfo androidViewInfo = get(AndroidViewInfo.class);
        PagerInfo pagerInfo = androidViewInfo.mapPagers.get(pagerElement);
        planStr.append(String.format("checkVisibility %d |", pagerInfo.id));
        final ViewInfo viewInfo = ViewInfo.of(pagerInfo.id);
        runnables.add(new Runnable() {
            @Override
            public void run() {
                system().checkVisibility(viewInfo);
            }
        });
        return newTransition();
    }


    @SystemAction
    public StateTransition checkElementText(Element element) {
        AndroidViewInfo androidViewInfo = get(AndroidViewInfo.class);
        TextInfo textInfo = get(TextInfo.class);
        final ViewInfo viewInfo = androidViewInfo.mapElements.get(element);
        final ElementText textToCheck = textInfo.getText(element);
        planStr.append(String.format("checkText %d is %s|", viewInfo.id, textToCheck));
        runnables.add(new Runnable() {
            @Override
            public void run() {
                system().checkText(viewInfo, textToCheck);
            }
        });
        return newTransition();
    }

    @SystemAction
    public StateTransition setElementText(Element element) {
        AndroidViewInfo androidViewInfo = get(AndroidViewInfo.class);
        final ViewInfo viewInfo = androidViewInfo.mapElements.get(element);
        TextInfo textInfo = get(TextInfo.class);
        final ElementInputText inputText = textInfo.getInputText(element);
        planStr.append(String.format("inputText %d with %s|", viewInfo.id, inputText));
        runnables.add(new Runnable() {
            @Override
            public void run() {
                system().inputText(viewInfo, inputText);
            }
        });
        return newTransition();
    }

    @SystemAction
    public StateTransition clickElement(Element element) {
        AndroidViewInfo androidViewInfo = get(AndroidViewInfo.class);
        final ViewInfo viewInfo = androidViewInfo.mapElements.get(element);
        planStr.append(String.format("performClick %d |", viewInfo.id));
        runnables.add(new Runnable() {
            @Override
            public void run() {
                system().clickElement(viewInfo);
            }
        });

        return newTransition();
    }

    @SystemAction
    public StateTransition backAt(Screen screen) {
        AndroidViewInfo androidViewInfo = get(AndroidViewInfo.class);
        ScreenInfo screenInfo = androidViewInfo.mapScreens.get(screen);
        planStr.append(String.format("perform Back from %s |", screenInfo.name));
        runnables.add(new Runnable() {
            @Override
            public void run() {
                system().performBack();
            }
        });
        return newTransition();
    }

    @SystemAction
    public StateTransition closeApp() {
        planStr.append("Close App |");
        planStr.append("\n");
        runnables.add(new Runnable() {
            @Override
            public void run() {
                system().closeApp();
            }
        });
        runPlan();
        return newTransition();
    }

    private void runPlan() {
        try {
            for (Runnable runnable : runnables) {
                runnable.run();
            }
        } catch (Throwable throwable) {
            String message = String.format("Failed plan: %s", planStr.toString());
            throw new RuntimeException(message, throwable);
        } finally {
            planStr = new StringBuilder();
            runnables.clear();
        }
    }

    private AndroidSystem system() {
        return get(AndroidSystem.class);
    }

    private <T> T get(Class<T> tClass) {
        return obtain(tClass.toString(), tClass);
    }

    private StateTransition newTransition() {
        SearchInfo searchInfo = get(SearchInfo.class);
        return searchInfo.isDebug ? new DebugStateTransition() : new GStateTransition();
    }
}
