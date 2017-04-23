package com.drk.tools.contextandroid.domain;

import com.drk.tools.contextandroid.variables.Element;
import com.drk.tools.contextandroid.variables.PagerElement;
import com.drk.tools.contextandroid.variables.Screen;
import com.drk.tools.gplannercore.core.Atom;

public class HierarchyInfo {


    public Screen screenOf(Element element) {
        return null;
    }

    public boolean belongsToScreen(Element element) {
        return false;
    }

    public boolean belongsToPager(Element element) {
        return false;
    }

    public Atom<Enum> pageOf(Element element) {
        return null;
    }

    public Enum pagerOf(Element element) {
        return null;
    }

    public Screen screenOf(PagerElement pagerElement) {
        return null;
    }

    public boolean belongsToScreen(PagerElement pagerElement) {
        return false;

    }
}
