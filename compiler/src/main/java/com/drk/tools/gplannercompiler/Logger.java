package com.drk.tools.gplannercompiler;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

public class Logger {

    private final static String MESSAGE = "%s | %s";
    private final Messager messager;

    Logger(Messager messager) {
        this.messager = messager;
    }

    public void log(Object obj, String content) {
        String message = String.format(MESSAGE, getFrom(obj), content);
        messager.printMessage(Diagnostic.Kind.NOTE, message);
    }

    public void error(Object obj, Throwable throwable) {
        String message = String.format(MESSAGE, getFrom(obj), throwable.getMessage());
        messager.printMessage(Diagnostic.Kind.ERROR, message);
    }

    private String getFrom(Object obj) {
        return obj.getClass().getSimpleName();
    }
}
