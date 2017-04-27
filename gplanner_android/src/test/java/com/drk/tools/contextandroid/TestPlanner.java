package com.drk.tools.contextandroid;

import com.drk.tools.contextandroid.domain.Scenario;
import org.junit.Test;

public class TestPlanner {

    @Test
    public void testPlanner() throws Throwable {
        AppChecker planner = new AppChecker(ScreenDefinition.build(), new TestAndroidSystem(), true);
        Scenario scenario = Scenario.builder()
                .mock(MyMocks.TEST1)
                .clicked(1)
                .checkAt(ScreenDefinition.SCREEN_NEWS_DETAILS)
                .build();
        planner.assertScenario(scenario, 3);
    }
}
