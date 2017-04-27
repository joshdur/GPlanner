package com.drk.tools.contextandroid;

import com.drk.tools.contextandroid.domain.AndroidViewInfo;
import com.drk.tools.contextandroid.domain.NavigationInfo;
import com.drk.tools.contextandroid.domain.ScreenInfo;
import com.drk.tools.contextandroid.domain.ViewInfo;

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
                .addView(ViewInfo.builder(1)
                        .clickActionInfo(NavigationInfo.changeToScreen(SCREEN_NEWS_DETAILS))
                        .build())
                .build();
    }


    private static ScreenInfo newsDetailsScreen() {
        return ScreenInfo.builder(SCREEN_NEWS_DETAILS)
                .addView(ViewInfo.builder(2).build())
                .back(NavigationInfo.changeToScreen(SCREEN_LIST_NEWS))
                .build();
    }
}
