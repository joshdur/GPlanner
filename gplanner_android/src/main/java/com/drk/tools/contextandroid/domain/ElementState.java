package com.drk.tools.contextandroid.domain;

public class ElementState {

    public enum State {
        VISIBLE,
        INVISIBLE,
        DISPLAYED,
        NON_DISPLAYED
    }

    public final int resId;
    public final State state;

    public ElementState(int resId, State state) {
        this.resId = resId;
        this.state = state;
    }
}
