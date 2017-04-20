package com.drk.tools.gplannercompiler.gen.unifier;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.GenException;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import java.util.*;


public class UnifierGenerator {

    private final Set<? extends Element> operators;
    private final Set<? extends Element> systemActions;
    private final Logger logger;

    public UnifierGenerator(Set<? extends Element> operators, Set<? extends Element> systemActions, Logger logger) {
        this.operators = operators;
        this.systemActions = systemActions;
        this.logger = logger;
    }

    public void generate() throws GenException {
        logger.log(this, "Generate...");
        check();
        logger.log(this, "Building Type Unifiers");
        List<TypeUnifier> typeUnifiers = buildTypeUnifiers();
        logger.log(this, "Generate Unifiers");

    }

    private List<TypeUnifier> buildTypeUnifiers() {
        HashMap<String, TypeUnifier> hashTypes = new LinkedHashMap<>();
        for (Element operator : operators) {
            String name = operator.getSimpleName().toString();
            TypeUnifier typeUnifier = hashTypes.get(name);
            if (typeUnifier == null) {
                typeUnifier = new TypeUnifier();
                hashTypes.put(name, typeUnifier);
            }
            typeUnifier.setOperator((ExecutableElement) operator);
        }
        for (Element action : systemActions) {
            String name = action.getSimpleName().toString();
            TypeUnifier typeUnifier = hashTypes.get(name);
            if (typeUnifier == null) {
                typeUnifier = new TypeUnifier();
                hashTypes.put(name, typeUnifier);
            }
            typeUnifier.setSystemAction((ExecutableElement) action);
        }
        return new ArrayList<>(hashTypes.values());
    }


    private void check() throws GenException {
        logger.log(this, "Checking operators class");
        for (Element operator : operators) {
            checkMethod(operator);
        }
        logger.log(this, "Checking systemActions class");
        for (Element action : systemActions) {
            checkMethod(action);
        }
    }

    private void checkMethod(Element operator) throws GenException {
        String name = operator.getSimpleName().toString();
        if (operator.getKind() != ElementKind.METHOD) {
            throw new GenException(name + " is not a method");
        }
        ExecutableElement method = (ExecutableElement) operator;
        if (!isAccessible(method)) {
            throw new GenException(name + " is not accessible");
        }
        if (isStatic(method)) {
            throw new GenException(name + " should not be static");
        }
    }

    private boolean isAccessible(ExecutableElement method) {
        Set<Modifier> methodModifiers = method.getModifiers();
        return methodModifiers.contains(Modifier.PUBLIC)
                || (!methodModifiers.contains(Modifier.PRIVATE) && !methodModifiers.contains(Modifier.PROTECTED));
    }

    private boolean isStatic(ExecutableElement method) {
        Set<Modifier> methodModifiers = method.getModifiers();
        return methodModifiers.contains(Modifier.STATIC);
    }

}
