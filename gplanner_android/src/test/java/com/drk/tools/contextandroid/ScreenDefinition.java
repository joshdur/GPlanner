package com.drk.tools.contextandroid;

import com.drk.tools.contextandroid.domain.*;

public class ScreenDefinition {

    public static String SCREEN_LIST_NEWS = "list_news";
    public static String SCREEN_NEWS_DETAILS = "news_details";

    public static AndroidViewInfo build() {
        return AndroidViewInfo.builder()
                .addMocks(MyMocks.values())
                .addScreen(listNewsScreen())
                .addScreen(newsDetailsScreen())
                .build();
    }

    private static ScreenInfo listNewsScreen() {
        return ScreenInfo.builder(SCREEN_LIST_NEWS)
                .addView(ViewInfo.builder()
                        .id(1)
                        .click(Action.changeToScreen(SCREEN_NEWS_DETAILS))
                        .build())
                .build();
    }


    private static ScreenInfo newsDetailsScreen() {
        return ScreenInfo.builder(SCREEN_NEWS_DETAILS)
                .addView(ViewInfo.builder()
                        .id(2)
                        .click(Action.launchIntent(IntentData.withAction("android.view")))
                        .build())
                .back(Action.changeToScreen(SCREEN_LIST_NEWS))
                .build();
    }
}
