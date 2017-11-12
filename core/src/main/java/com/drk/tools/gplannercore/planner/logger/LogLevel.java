package com.drk.tools.gplannercore.planner.logger;

public enum LogLevel {
    NONE,
    ERROR,
    DEBUG,
    VERBOSE;


    public boolean isOver(LogLevel logLevel){
        return ordinal() >= logLevel.ordinal();
    }
}
