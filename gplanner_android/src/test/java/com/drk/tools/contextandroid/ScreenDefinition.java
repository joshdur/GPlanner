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
        ViewInfo loading = ViewInfo.of(1);
        return ScreenInfo.builder(SCREEN_LIST_NEWS)
                .addView(ViewInfo.of(2))
                .addView(ViewInfo.builder()
                        .id(3)
                        .showsAfter(loading)
                        .build())
                .addView(ViewInfo.builder()
                        .id(4)
                        .showsAfter(loading)
                        .click(Action.changeToScreen(SCREEN_NEWS_DETAILS))
                        .build())
                .addView(ViewInfo.builder()
                        .id(5)
                        .click(Action.addViews(ViewInfo.builder()
                                .hint("Search by title")
                                .imeOptionsClickAction(Action.addViews(ViewInfo.builder()
                                        .id(6)
                                        .showsAfter(loading)
                                        .build()))
                                .build()))
                        .build())
                .build();
    }

    private static ScreenInfo newsDetailsScreen() {
        return ScreenInfo.builder(SCREEN_NEWS_DETAILS)
                .addView(ViewInfo.builder()
                        .id(7)
                        .click(Action.launchIntent(IntentData.withAction("action")))
                        .build())
                .back(Action.changeToScreen(SCREEN_LIST_NEWS))
                .build();
    }

}
