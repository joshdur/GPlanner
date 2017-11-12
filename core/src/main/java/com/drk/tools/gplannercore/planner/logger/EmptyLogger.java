package com.drk.tools.gplannercore.planner.logger;

public class EmptyLogger implements Logger {

    @Override
    public void log(String label, String message) {
        //Do nothing
    }

    @Override
    public void log(String label, String message, Throwable throwable) {
        //Do nothing
    }
}
