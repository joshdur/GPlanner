package com.drk.tools.gplannercore.planner;

import com.drk.tools.gplannercore.core.Plan;
import com.drk.tools.gplannercore.core.streams.GInputStream;
import com.drk.tools.gplannercore.core.streams.GStreamException;

import java.util.ArrayList;
import java.util.List;

public class PlanStream {

    public enum State {
        OPEN,
        CLOSED
    }

    private final List<Plan> planList = new ArrayList<>();
    private final GInputStream inputStream;
    private final int size;
    private State state;
    private Throwable throwable;

    PlanStream(GInputStream inputStream, int size) {
        this.inputStream = inputStream;
        this.size = size;
        this.state = State.OPEN;
    }

    public List<Plan> read() {
        if (state == State.CLOSED) {
            return new ArrayList<>();
        }
        try {
            readToBuffer();
        } catch (GStreamException e) {
            throwable = e;
            close();
        }
        List<Plan> plans = new ArrayList<>(planList);
        planList.clear();
        return plans;
    }

    private void readToBuffer() throws GStreamException {
        while (planList.size() < size) {
            Plan plan = inputStream.read();
            if (plan != null) {
                planList.add(plan);
            } else {
                close();
                return;
            }
        }
    }

    public void close() {
        if (state == State.OPEN) {
            inputStream.close();
            state = State.CLOSED;
        }
    }

    void setError(Throwable throwable) {
        this.throwable = throwable;
    }

    public State getState() {
        return state;
    }

    public Throwable getError() {
        return throwable;
    }

    public boolean hasError(){
        return throwable != null;
    }
}
