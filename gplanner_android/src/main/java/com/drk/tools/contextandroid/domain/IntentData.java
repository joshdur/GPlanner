package com.drk.tools.contextandroid.domain;

public class IntentData {

    public final String action;
    public final String aPackage;
    public final String component;
    public final String uri;

    private IntentData(String action, String aPackage, String component, String uri) {
        this.action = action;
        this.aPackage = aPackage;
        this.component = component;
        this.uri = uri;
    }

    public static IntentData withAction(String action) {
        return new IntentData(action, null, null, null);
    }

    public static IntentData toPackage(String toPackage) {
        return new IntentData(null, toPackage, null, null);
    }

    public static IntentData withComponent(String className) {
        return new IntentData(null, null, className, null);
    }

    public static IntentData withUri(String uri) {
        return new IntentData(null, null, null, uri);
    }

    @Override
    public String toString() {
        return String.format("with action: %s package %s component %s uri %s", action, aPackage, component, uri);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntentData) {
            IntentData other = (IntentData) obj;
            return equalStr(action, other.action)
                    && equalStr(aPackage, other.aPackage)
                    && equalStr(component, other.component)
                    && equalStr(uri, other.uri);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int code = action != null ? action.hashCode() : 1;
        code *= aPackage != null ? aPackage.hashCode() : 1;
        code *= component != null ? component.hashCode() : 1;
        code *= uri != null ? uri.hashCode() : 1;
        return code;
    }

    private boolean equalStr(String a, String b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }
}
