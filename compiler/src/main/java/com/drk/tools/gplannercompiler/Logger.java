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
        String thMessage = getMessage(throwable);
        String message = String.format(MESSAGE, getFrom(obj), thMessage);
        messager.printMessage(Diagnostic.Kind.ERROR, message);
    }

    private static String getMessage(Throwable cause) {
        String message = cause.getMessage();
        if (message == null || "".equals(message)) {
            message = cause.getClass().getSimpleName();
        }
        StackTraceElement[] stack = cause.getStackTrace();
        if (stack != null && stack.length > 0) {
            StackTraceElement stackElement = findProjectTrace(stack);
            message = String.format("%s at %s", message, stackElement.toString());
        }
        return message;
    }

    private static StackTraceElement findProjectTrace(StackTraceElement[] stack) {
        for (StackTraceElement aStack : stack) {
            if (aStack.toString().contains("drk.tools")) {
                return aStack;
            }
        }
        return stack[0];
    }

    private String getFrom(Object obj) {
        return obj.getClass().getSimpleName();
    }
}
