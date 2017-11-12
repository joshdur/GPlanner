package com.drk.tools.gplannercore.planner.logger;

public class DefaultLogger implements Logger {

    @Override
    public void log(String label, String message) {
        System.out.println(label + ": " + message);
    }

    @Override
    public void log(String label, String message, Throwable throwable) {
        log(label, message);
        log(label, throwable.getMessage());
    }
}
