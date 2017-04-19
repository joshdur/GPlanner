package com.drk.tools.gplannercore.planner;

import com.drk.tools.gplannercore.core.Plan;
import com.drk.tools.gplannercore.core.streams.GInputStream;
import com.drk.tools.gplannercore.core.streams.GStreamException;

import java.util.ArrayList;
import java.util.List;

public class PlanStream {

    public enum State {
        OPEN,
        CLOSED,
        ERROR
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

    public List<Plan> read() throws GStreamException {
        if (state != State.OPEN) {
            throw new GStreamException("Stream is Closed");
        }
        while (planList.size() < size && state == State.OPEN) {
            Plan plan = inputStream.read();
            if (plan != null) {
                planList.add(plan);
            } else {
                close();
            }
        }
        List<Plan> plans = new ArrayList<>(planList);
        planList.clear();
        return plans;
    }

    public void close() {
        if (state == State.OPEN) {
            inputStream.close();
            state = State.CLOSED;
        }
    }

    void setError(Throwable throwable) {
        this.throwable = throwable;
        this.state = State.ERROR;
    }

    public State getState() {
        return state;
    }

    public Throwable getError() {
        return throwable;
    }
}
