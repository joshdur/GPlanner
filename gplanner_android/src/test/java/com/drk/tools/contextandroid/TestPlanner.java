package com.drk.tools.contextandroid;

import com.drk.tools.gplannercore.core.Plan;
import org.junit.Test;

import java.util.List;

public class TestPlanner {

    @Test
    public void testPlanner() {
        AndroidViewPlanner planner = new AndroidViewPlanner(ScreenDefinition.build(), true);
        PathTokens pathTokens = PathTokens.builder()
                .mock(MyMocks.TEST1)
                .clicked(1)
                .checkAt(ScreenDefinition.SCREEN_NEWS_DETAILS)
                .build();
        List<Plan> plans = planner.search(pathTokens);
    }
}
