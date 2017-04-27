package com.drk.tools.contextandroid;

import com.drk.tools.contextandroid.domain.ScreenInfo;
import com.drk.tools.contextandroid.domain.ViewInfo;

public interface AndroidSystem {

    void mock(Enum reference);

    void launchApp(ScreenInfo screenInfo);

    void checkScreen(ScreenInfo screenInfo);

    void checkVisibility(ViewInfo viewInfo);

    void checkText(ViewInfo viewInfo, String text);

    void inputText(ViewInfo viewInfo, String text);

    void clickElement(ViewInfo viewInfo);

    void performBack();

    void closeApp();

}
