package com.drk.tools.contextandroid;

import com.drk.tools.contextandroid.domain.*;

public interface AndroidSystem {

    void mock(Enum reference);

    void launchApp(ScreenInfo screenInfo);

    void checkIntent(IntentData intentData);

    void checkScreen(ScreenInfo screenInfo);

    void checkVisibility(ViewInfo viewInfo);

    void checkElementState(ViewInfo viewInfo, ElementState elementState);

    void checkText(ViewInfo viewInfo, ElementText text);

    void inputText(ViewInfo viewInfo, ElementInputText text);

    void clickElement(ViewInfo viewInfo);

    void performBack();

    void closeApp();

}
