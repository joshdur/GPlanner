package com.drk.tools.gplannercore.planner.search;

import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.Plan;
import com.drk.tools.gplannercore.core.state.State;

import com.drk.tools.gplannercore.core.state.Transition;
import com.drk.tools.gplannercore.core.streams.GOutputStream;
import com.drk.tools.gplannercore.core.streams.GStreamException;
import com.drk.tools.gplannercore.planner.search.structures.DataWrapper;
import com.drk.tools.gplannercore.planner.search.structures.Node;

public class Search {

    private final DataWrapper dataWrapper;
    private final NodeBuilder nodeBuilder;
    private final long timeout;
    private final boolean continueAfterFirst;
    private final boolean isOnline;
    private long startTime;

    public Search(DataWrapper dataWrapper, NodeBuilder nodeBuilder, long timeout, boolean continueAfterFirst, boolean online) {
        this.dataWrapper = dataWrapper;
        this.nodeBuilder = nodeBuilder;
        this.timeout = timeout;
        this.continueAfterFirst = continueAfterFirst;
        this.isOnline = online;
    }

    void start(Context context, State initial, State finalState, GOutputStream outputStream) throws SearchException {
        startTimer();
        Node root = nodeBuilder.build(null, initial, null);
        dataWrapper.push(root);
        boolean keepGoing = true;
        try {
            while (!dataWrapper.isEmpty() && keepGoing && !outputStream.isClosed()) {
                assertRemainingTime();
                Node node = dataWrapper.first();
                Transition transition = node.nodeData.searchUnifier.next();
                if (transition != null) {
                    State newState = applyEffects(context, node.nodeData.state, transition);
                    if (!NodeUtils.existsInSequence(newState, node)) {
                        Node newNode = nodeBuilder.build(node, newState, transition);
                        if (StateUtils.validate(newState, finalState)) {
                            keepGoing = pushPlan(newNode, outputStream);
                        } else {
                            dataWrapper.push(newNode);
                        }
                    }
                }
            }
            closeStream(outputStream);
        }catch (SearchException e){
           closeStream(outputStream);
           throw e;
        }
    }

    private void closeStream(GOutputStream outputStream) throws SearchException {
        try{
            outputStream.close();
        }catch (GStreamException e) {
            throw new SearchException(e);
        }
    }

    private boolean pushPlan(Node node, GOutputStream outputStream) throws SearchException {
        try {
            Plan plan = NodeUtils.recoverSequence(node);
            outputStream.write(plan, getRemainingTime());
            return continueAfterFirst;
        }catch (GStreamException e){
            throw new SearchException(e);
        }
    }

    private State applyEffects(Context context, State state, Transition transition) throws SearchException {
        State newState = state.clone();
        if(isOnline) {
            try {
                Transition eventTransition = context.execute(transition);
                StateUtils.applyEffects(newState, eventTransition.stateTransition);
            }catch (Throwable e){
                throw new SearchException(e);
            }
        }
        StateUtils.applyEffects(newState, transition.stateTransition);
        return newState;
    }

    private void startTimer() {
        startTime = System.currentTimeMillis();
    }

    private void assertRemainingTime() throws SearchException {
        if (getRemainingTime() <= 0) {
            throw new SearchException("Timout");
        }
    }

    private long getRemainingTime() {
        return timeout == -1 ? Long.MAX_VALUE : calcRemainingTime();
    }

    private long calcRemainingTime() {
        long elapsed = System.currentTimeMillis() - startTime;
        long remainingTime = timeout - elapsed;
        return remainingTime > 0 ? remainingTime : 0;
    }

    interface NodeBuilder {

        Node build(Node lastNode, State newState, Transition transition);
    }
}
