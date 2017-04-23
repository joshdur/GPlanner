package com.drk.tools.contextandroid.atoms;

import com.drk.tools.contextandroid.variables.*;
import com.drk.tools.gplannercore.core.Atom;

public class MainAtoms {

    public static IsMocked isMocked = new IsMocked();
    public static IsAppLaunched isAppLaunched = new IsAppLaunched();
    public static IsSearchFinished isSearchFinished = new IsSearchFinished();
    public static At at = new At();
    public static ScreenChecked screenChecked = new ScreenChecked();
    public static ElementVisible elementVisible = new ElementVisible();
    public static PagerElementVisible pagerElementVisible = new PagerElementVisible();
    public static ElementTextChecked elementTextChecked = new ElementTextChecked();
    public static ElementClicked elementClicked = new ElementClicked();
    public static ElementTextSet elementTextSet = new ElementTextSet();
    public static BackAt backAt = new BackAt();

    public static class IsMocked extends Atom<Bool> {

    }

    public static class IsAppLaunched extends Atom<Bool> {

    }

    public static class IsSearchFinished extends Atom<Bool> {

    }

    public static class At extends Atom<Screen> {

    }

    public static class ElementVisible extends Atom<Element> {

    }

    public static class PagerElementVisible extends Atom<PagerElement> {

    }

    public static class ElementTextChecked extends Atom<Element> {

    }

    public static class ElementClicked extends Atom<Element> {

    }

    public static class ElementTextSet extends Atom<Element> {

    }

    public static class BackAt extends Atom<Screen> {

    }

    public static class ScreenChecked extends Atom<Screen> {

    }
}
