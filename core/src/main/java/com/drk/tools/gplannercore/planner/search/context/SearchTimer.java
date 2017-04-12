package com.drk.tools.gplannercore.planner.search.context;

class SearchTimer {

    private final long timeout;
    private long startTime = -1;

    SearchTimer(long timeout) {
        this.timeout = timeout;
    }

    void startTimer() {
        startTime = System.currentTimeMillis();
    }

    long getRemainingTime() {
        return timeout == -1 ? Long.MAX_VALUE : calcRemainingTime();
    }

    private long calcRemainingTime() {
        if (startTime == -1) {
            return Long.MAX_VALUE;
        }
        long elapsed = System.currentTimeMillis() - startTime;
        long remainingTime = timeout - elapsed;
        return remainingTime > 0 ? remainingTime : 0;
    }

}
