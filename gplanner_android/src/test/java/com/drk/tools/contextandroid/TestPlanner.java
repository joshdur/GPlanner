package com.drk.tools.contextandroid;

import com.drk.tools.contextandroid.domain.ElementState;
import com.drk.tools.contextandroid.domain.Scenario;
import com.drk.tools.contextandroid.domain.ViewInfo;
import org.junit.Test;

public class TestPlanner {

    @Test
    public void testPlanner() throws Throwable {
        AppChecker planner = new AppChecker(ScreenDefinition.build(), new TestAndroidSystem(), true);
        Scenario scenario = Scenario.builder()
                .withMocked(MyMocks.TEST1)
                .withElementClicked(1)
                .withCheckedScreen(ScreenDefinition.SCREEN_NEWS_DETAILS)
                .build();
        planner.assertScenario(scenario, 3);
    }

    @Test
    public void testIntent() throws Throwable {
        AppChecker planner = new AppChecker(ScreenDefinition.build(), new TestAndroidSystem());
        Scenario scenario = Scenario.builder()
                .withMocked(MyMocks.TEST1)
                .withCheckedScreen(ScreenDefinition.SCREEN_NEWS_DETAILS)
                .withElementClicked(2)
                .build();
        planner.assertScenario(scenario, 3);
    }

    @Test
    public void testImeOptionsClicked() throws Throwable{
        AppChecker planner = new AppChecker(ScreenDefinition.build(), new TestAndroidSystem(), false);
        Scenario scenario = Scenario.builder()
                .withMocked(MyMocks.TEST1)
                .withCheckedScreen(ScreenDefinition.SCREEN_LIST_NEWS)
                .withElementClicked(5)
                .withInputText(ViewInfo.builder()
                        .hint("Search by title")
                        .build(), "papa", true)
                .withElementState(ViewInfo.of(6), ElementState.State.NON_DISPLAYED)
                .build();
        planner.assertScenario(scenario, 1);
    }
}
