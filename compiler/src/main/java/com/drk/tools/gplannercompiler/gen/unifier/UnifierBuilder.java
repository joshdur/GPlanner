package com.drk.tools.gplannercompiler.gen.unifier;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercompiler.gen.base.Spec;
import com.drk.tools.gplannercompiler.gen.base.SpecBuilder;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.*;


public class UnifierBuilder implements SpecBuilder {

    private final Set<? extends Element> operators;
    private final Set<? extends Element> systemActions;
    private final HashMap<String, String> ranges;
    private final Logger logger;

    public UnifierBuilder(Set<? extends Element> operators, Set<? extends Element> systemActions, HashMap<String, String> ranges, Logger logger) {
        this.operators = operators;
        this.systemActions = systemActions;
        this.logger = logger;
        this.ranges = ranges;
    }

    @Override
    public List<? extends Spec> buildSpecs() throws GenException {
        List<Spec> specs = new ArrayList<>();
        for (TypeUnifier type : buildTypes()) {
            specs.add(new SpecUnifier(type, logger));
        }
        return specs;
    }

    private List<TypeUnifier> buildTypes() throws GenException {
        HashMap<String, Container> hashTypes = new LinkedHashMap<>();
        for (Element operator : operators) {
            addToHash(operator, hashTypes, true);
        }
        for (Element action : systemActions) {
            addToHash(action, hashTypes, false);
        }
        List<TypeUnifier> types = new ArrayList<>();
        for (Container container : hashTypes.values()) {
            if (container.hasValidOperator()) {
                checkSameVariables(container);
                types.add(new TypeUnifier(container.name, container.operator, container.systemAction, container.operatorVariables, ranges));
            }
        }
        return types;
    }

    private void checkSameVariables(UnifierBuilder.Container container) throws GenException {
        if (!container.hasValidSystemAction()) {
            return;
        }
        if (container.operatorVariables.size() != container.systemActionVariables.size()) {
            throw new GenException(container.name + " does not hame the same number of variables");
        }
        for (int i = 0; i < container.operatorVariables.size(); i++) {
            VariableElement oElement = container.operatorVariables.get(i);
            VariableElement sElement = container.systemActionVariables.get(i);
            if (!oElement.asType().toString().equals(sElement.asType().toString())) {
                throw new GenException(String.format("Parameter %d in %s is not coincident between operator and system action", i, container.name));
            }
        }
    }

    private void addToHash(Element element, HashMap<String, Container> hash, boolean operator) {
        String name = element.getSimpleName().toString();
        Container container = hash.get(name);
        if (container == null) {
            container = new Container();
            container.name = name;
            hash.put(name, container);
        }
        ExecutableElement executableElement = (ExecutableElement) element;
        if (operator) {
            container.operator = executableElement;
            container.operatorVariables = executableElement.getParameters();
        } else {
            container.systemAction = executableElement;
            container.systemActionVariables = executableElement.getParameters();
        }
    }


    static class Container {
        String name;
        ExecutableElement operator;
        ExecutableElement systemAction;
        List<? extends VariableElement> operatorVariables;
        List<? extends VariableElement> systemActionVariables;

        boolean hasValidOperator() {
            return operator != null && operatorVariables != null;
        }

        boolean hasValidSystemAction() {
            return systemAction != null && systemActionVariables != null;
        }
    }
}
