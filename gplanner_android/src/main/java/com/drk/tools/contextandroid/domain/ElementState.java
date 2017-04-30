package com.drk.tools.contextandroid.domain;

public class ElementState {

    public enum State {
        VISIBLE,
        INVISIBLE,
        DISPLAYED,
        NON_DISPLAYED
    }

    public final ViewInfo info;
    public final State state;

    public ElementState(ViewInfo info, State state) {
        this.info = info;
        this.state = state;
    }
}
