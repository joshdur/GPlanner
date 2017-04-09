package com.drk.tools.gplannercore.core.main;

import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannercore.core.state.Transition;
import com.drk.tools.gplannercore.planner.state.GStateTransition;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseUnifier {

    private final String operatorName;
    private final int code;
    private final Enum[][] enums;
    private final StateCounter stateCounter;

    public BaseUnifier(String operatorName, Enum[]... enums) {
        this.operatorName = operatorName;
        this.code = operatorName.hashCode();
        this.enums = enums;
        this.stateCounter = new StateCounter(enums);
    }

    public int getCode() {
        return this.code;
    }

    public boolean hasNext() {
        return stateCounter.hasNext();
    }

    public Transition next() {
        List<String> enums = stateCounter.currentValues();
        StateTransition stateTransition = build(enums);
        stateCounter.next();
        return new Transition(code, stateCounter.getPosition(), stateTransition);
    }

    public String asString(Transition transition) {
        StateCounter stateCounter = new StateCounter(transition.variableStateCode, enums);
        return String.format("%s %s", operatorName, stateCounter.currentValues().toString());
    }

    public Transition execute(Transition transition) throws Throwable {
        StateCounter stateCounter = new StateCounter(transition.variableStateCode, enums);
        List<String> variables = stateCounter.currentValues();
        StateTransition stateTransition = execute(variables);
        return new Transition(code, transition.variableStateCode, stateTransition);
    }


    protected abstract StateTransition build(List<String> variables);

    protected StateTransition execute(List<String> variables) throws Throwable{
        return new GStateTransition();
    }

    private static class StateCounter {

        private List<Element> elements;
        private boolean hasFinished;
        private int position;

        StateCounter(Enum[]... enums) {
            this.elements = buildElements(enums);
            this.hasFinished = false;
            this.position = 0;
        }

        StateCounter(int position, Enum[]... enums) {
            this(enums);
            while (this.position != position && !hasFinished) {
                next();
            }
        }

        private List<Element> buildElements(Enum[]... enums) {
            List<Element> elements = new ArrayList<>();
            for (Enum[] e : enums) {
                elements.add(new Element(e));
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

        List<String> currentValues() {
            List<String> values = new ArrayList<>();
            for (Element element : elements) {
                values.add(element.value().name());
            }
            return values;
        }
    }

    private static class Element {

        private final Enum[] variables;
        private int index;

        Element(Enum[] variables) {
            this.variables = variables;
            this.index = 0;
        }

        void reset() {
            index = 0;
        }

        boolean isOver() {
            return index >= variables.length;
        }

        void next() {
            index++;
        }


        Enum value() {
            return variables[index];
        }
    }
}
