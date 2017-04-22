package com.drk.tools.gplannercompiler.gen.unifier;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercore.core.main.Operators;
import com.drk.tools.gplannercore.core.main.SystemActions;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.Set;

class UnifierChecker {

    private final Logger logger;

    UnifierChecker(Logger logger) {
        this.logger = logger;
    }

    void check(Set<? extends Element> operators, Set<? extends Element> systemActions) throws GenException {
        logger.log(this, "Checking operators class");
        for (Element operator : operators) {
            checkMethod(operator, Operators.class);
        }
        logger.log(this, "Checking systemActions class");
        for (Element action : systemActions) {
            checkMethod(action, SystemActions.class);
        }
    }

    private <T> void checkMethod(Element operator, Class<T> parent) throws GenException {
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
        checkParentExtendsFrom(method, parent);
    }

    private <T> void checkParentExtendsFrom(ExecutableElement element, Class<T> parent) throws GenException {
        TypeElement containerClass = (TypeElement) element.getEnclosingElement();
        TypeMirror containerSuperClass = containerClass.getSuperclass();
        if (!containerSuperClass.toString().equals(parent.getCanonicalName())) {
            String name = element.getSimpleName().toString();
            throw new GenException(String.format("Class of %s must implement %s", name, parent.getCanonicalName()));
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

    void checkSameVariables(UnifierGenerator.Container container) throws GenException {
        if (container.operatorVariables.size() != container.systemActionVariables.size()) {
            throw new GenException(container.name + " does not hame the same number of variables");
        }
        for (int i = 0; i < container.operatorVariables.size(); i++) {
            VariableElement oElement = container.operatorVariables.get(i);
            VariableElement sElement = container.systemActionVariables.get(i);
            if(!oElement.asType().toString().equals(sElement.asType().toString())){
                throw new GenException(String.format("Parameter %d in %s is not coincident between operator and system action", i, container.name));
            }
        }
    }
}
