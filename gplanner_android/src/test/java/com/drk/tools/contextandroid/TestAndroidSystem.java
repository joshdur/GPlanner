package com.drk.tools.contextandroid;

import com.drk.tools.contextandroid.domain.*;

public class TestAndroidSystem implements AndroidSystem {
    @Override
    public void mock(Enum reference) {
        System.out.print("mock - ");
    }

    @Override
    public void launchApp(ScreenInfo screenInfo) {
        System.out.print("launchApp - ");
    }

    @Override
    public void checkIntent(IntentData intentData) {
        System.out.print("checkIntent - ");
    }

    @Override
    public void checkScreen(ScreenInfo screenInfo) {
        System.out.print("checkScreen - ");
    }

    @Override
    public void checkVisibility(ViewInfo viewInfo) {
        System.out.print("checkVisibility - ");
    }

    @Override
    public void checkElementState(ViewInfo viewInfo, ElementState elementState) {
        System.out.print("checkElementState - ");
    }

    @Override
    public void checkText(ViewInfo viewInfo, ElementText text) {
        System.out.print("checkText - ");
    }

    @Override
    public void inputText(ViewInfo viewInfo, ElementInputText text) {
        System.out.print("inputText - ");
    }

    @Override
    public void clickElement(ViewInfo viewInfo) {
        System.out.print("clickElement - ");
    }

    @Override
    public void performBack() {
        System.out.print("performBack - ");
    }

    @Override
    public void closeApp() {
        System.out.print("closeApp - \n");
    }
}
