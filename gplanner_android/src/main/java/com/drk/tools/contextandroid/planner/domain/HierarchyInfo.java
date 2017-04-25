package com.drk.tools.contextandroid.planner.domain;

import com.drk.tools.contextandroid.planner.variables.Element;
import com.drk.tools.contextandroid.planner.variables.PagerElement;
import com.drk.tools.contextandroid.planner.variables.Screen;
import com.drk.tools.gplannercore.core.Atom;

import java.util.HashMap;

public class HierarchyInfo {

    private final HashMap<Element, Parent> hashParents;
    private final HashMap<PagerElement, Parent> hashPagerParent;

    public HierarchyInfo(HashMap<Element, Parent> hashParents, HashMap<PagerElement, Parent> hashPagerParent) {
        this.hashParents = hashParents;
        this.hashPagerParent = hashPagerParent;
    }

    public Screen screenOf(Element element) {
        Parent parent = hashParents.get(element);
        return parent.screen;
    }

    public boolean belongsToScreen(Element element) {
        Parent parent = hashParents.get(element);
        return parent != null && parent.belongsToScreen();
    }

    public boolean belongsToPager(Element element) {
        Parent parent = hashParents.get(element);
        return parent != null && parent.belongsToPager();
    }

    public Atom<Enum> pageOf(Element element) {
        // Parent parent = hashParents.get(element);
        // return parent.page;
        return null;
    }

    public PagerElement pagerOf(Element element) {
        Parent parent = hashParents.get(element);
        return parent.pager;
    }

    public Screen screenOf(PagerElement pagerElement) {
        Parent parent = hashPagerParent.get(pagerElement);
        return parent.screen;
    }

    public boolean belongsToScreen(PagerElement pagerElement) {
        Parent parent = hashPagerParent.get(pagerElement);
        return parent != null && parent.belongsToScreen();
    }

    public static class Parent {

        public final Screen screen;
        public final PagerElement pager;
        public final int page;

        public Parent(Screen screen, PagerElement pager, int page) {
            this.screen = screen;
            this.pager = pager;
            this.page = page;
        }

        boolean belongsToScreen() {
            return screen != null;
        }

        boolean belongsToPager() {
            return pager != null;
        }
    }
}
