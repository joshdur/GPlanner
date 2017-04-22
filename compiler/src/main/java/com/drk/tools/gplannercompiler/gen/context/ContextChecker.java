package com.drk.tools.gplannercompiler.gen.context;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercore.core.main.Operators;
import com.drk.tools.gplannercore.core.main.SystemActions;

import javax.lang.model.element.*;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Set;

class ContextChecker {

    private final Logger logger;
    private final Types types;

    ContextChecker(Logger logger, Types types) {
        this.logger = logger;
        this.types = types;
    }


    void check(Set<? extends Element> unifiers) throws GenException {
        logger.log(this, "Checking unifier classes");
        for (Element element : unifiers) {
            checkUnifier(element);
        }
    }

    private void checkUnifier(Element element) throws GenException {
        String name = element.getSimpleName().toString();
        if (element.getKind() != ElementKind.CLASS) {
            throw new GenException(String.format("%s should be a class", name));
        }
        TypeElement unifier = (TypeElement) element;
        List<? extends Element> elements = unifier.getEnclosedElements();
        int countConstructors = 0;
        ExecutableElement constructor = null;
        for (Element unifierElement : elements) {
            if (unifierElement.getKind() == ElementKind.CONSTRUCTOR) {
                countConstructors++;
                constructor = (ExecutableElement) unifier;
            }
        }
        if (countConstructors > 1 || countConstructors == 0) {
            throw new GenException(String.format("%s must have one constructor", name));
        }
        checkConstructor(name, constructor);
    }

    private void checkConstructor(String name, ExecutableElement constructor) throws GenException {
        Set<Modifier> modifiers = constructor.getModifiers();
        if (!modifiers.contains(Modifier.PUBLIC)) {
            throw new GenException(String.format("Constructor in %s must be public", name));
        }
        List<? extends VariableElement> parameters = constructor.getParameters();
        if (parameters.size() > 2 || parameters.size() == 0) {
            throw new GenException(String.format("Constructor in %s should have one Operators parameter and optional SystemActions parameter", name));
        }
        int countOperators = 0;
        int countSystemActions = 0;
        for (VariableElement variableElement : parameters) {
            TypeElement variableType = (TypeElement) types.asElement(variableElement.asType());
            if (variableType.getSuperclass().toString().equals(Operators.class.getCanonicalName())) {
                countOperators++;
            } else if (variableType.getSuperclass().toString().equals(SystemActions.class.getCanonicalName())) {
                countSystemActions++;
            } else {
                String type = variableType.asType().toString();
                throw new GenException(String.format("%s in %s is not allowed in unifier constructor", type, name));
            }
        }
        if (countOperators > 1 || countSystemActions > 1) {
            throw new GenException(String.format("Constructor in %s should have one Operators parameter and optional SystemActions parameter", name));
        }
    }
}
