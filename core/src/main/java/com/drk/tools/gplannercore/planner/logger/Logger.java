package com.drk.tools.gplannercore.planner.logger;

public interface Logger {

    void log(String label, String message);

    void log(String label, String message, Throwable throwable);

}
