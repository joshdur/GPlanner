package com.drk.tools.gplannercore.core.main;

import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannercore.core.state.Transition;
import com.drk.tools.gplannercore.core.variables.Variable;
import com.drk.tools.gplannercore.core.variables.VariableRange;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseUnifier {

    private final Context context;
    private final String operatorName;
    private final List<VariableRange> variableRanges;
    private final Integer[] counts;
    private final int code;
    private final StateCounter stateCounter;

    public BaseUnifier(Context context, String operatorName) {
        this.context = context;
        this.operatorName = operatorName;
        this.variableRanges = getVariables();
        this.counts = getCounts();
        this.code = operatorName.hashCode();
        this.stateCounter = new StateCounter(counts);
    }

    protected <T> T operatorClass(Class<T> tClass) {
        return context.injectOperator(tClass);
    }

    protected abstract List<VariableRange> getVariables();

    private Integer[] getCounts() {
        List<Integer> counts = new ArrayList<>();
        for (VariableRange variableRange : variableRanges) {
            counts.add(variableRange.count());
        }
        return counts.toArray(new Integer[]{});
    }

    public int getCode() {
        return this.code;
    }

    public boolean hasNext() {
        return stateCounter.hasNext();
    }

    public Transition next() {
        List<Integer> variablePositions = stateCounter.currentValues();
        StateTransition stateTransition = build(recoverVariables(variablePositions));
        int counterPosition = stateCounter.getPosition();
        stateCounter.next();
        return new Transition(code, counterPosition, stateTransition);
    }

    public String asString(Transition transition) {
        StateCounter stateCounter = new StateCounter(transition.variableStateCode, counts);
        List<Variable> variables = recoverVariables(stateCounter.currentValues());
        return String.format("%s %s", operatorName, variables.toString());
    }

    private List<Variable> recoverVariables(List<Integer> variablePositions) {
        List<Variable> variables = new ArrayList<>();
        for (int i = 0; i < variablePositions.size(); i++) {
            variables.add(variableAt(i, variablePositions.get(i)));
        }
        return variables;
    }

    private Variable variableAt(int index, int variablePosition) {
        return variableRanges.get(index).variableAt(variablePosition);
    }

    public Transition execute(Transition transition) throws Throwable {
        StateCounter stateCounter = new StateCounter(transition.variableStateCode, counts);
        List<Integer> variablePositions = stateCounter.currentValues();
        StateTransition stateTransition = execute(recoverVariables(variablePositions));
        return new Transition(code, transition.variableStateCode, stateTransition);
    }


    protected abstract StateTransition build(List<Variable> variables);

    protected abstract StateTransition execute(List<Variable> variables) throws Throwable;

    private static class StateCounter {

        private List<Element> elements;
        private boolean hasFinished;
        private int position;

        StateCounter(Integer... variableCounts) {
            this.elements = buildElements(variableCounts);
            this.hasFinished = false;
            this.position = 0;
        }

        StateCounter(int position, Integer... variableCounts) {
            this(variableCounts);
            while (this.position != position && !hasFinished) {
                next();
            }
        }

        private List<Element> buildElements(Integer... variableCounts) {
            List<Element> elements = new ArrayList<>();
            for (int count : variableCounts) {
                elements.add(new Element(count));
            }
            return elements;
        }

        boolean hasNext() {
            return !hasFinished;
        }

        int getPosition() {
            return position;
        }

        void next() {
            int elementIndex = 0;
            boolean isOver = true;
            while (isOver && elementIndex < elements.size()) {
                Element element = elements.get(elementIndex);
                element.next();
                isOver = element.isOver();
                if (isOver) {
                    element.reset();
                    elementIndex++;
                }
            }

            if (isOver) {
                hasFinished = true;
            }
            position++;
        }

        List<Integer> currentValues() {
            List<Integer> values = new ArrayList<>();
            for (Element element : elements) {
                values.add(element.value());
            }
            return values;
        }
    }

    private static class Element {

        private final int count;
        private int index;

        Element(int count) {
            this.count = count;
            this.index = 0;
        }

        void reset() {
            index = 0;
        }

        boolean isOver() {
            return index >= count;
        }

        void next() {
            index++;
        }

        int value() {
            return index;
        }
    }
}
