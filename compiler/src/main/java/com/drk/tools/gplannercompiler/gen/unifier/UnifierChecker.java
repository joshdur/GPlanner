package com.drk.tools.gplannercompiler.gen.unifier;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercompiler.gen.variables.support.Checker;
import com.drk.tools.gplannercore.core.main.Operators;
import com.drk.tools.gplannercore.core.main.SystemActions;
import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannercore.core.variables.Variable;

import javax.lang.model.element.*;
import javax.lang.model.util.Types;
import java.util.Set;

class UnifierChecker {

    private final Logger logger;
    private final Types types;

    UnifierChecker(Logger logger, Types types) {
        this.logger = logger;
        this.types = types;
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
        Checker.assertIsMethod(operator);
        Checker.assertHasNotModifiers(operator, Modifier.PRIVATE, Modifier.PROTECTED, Modifier.STATIC);
        ExecutableElement executableElement = (ExecutableElement) operator;
        for (Element element : executableElement.getParameters()) {
            Checker.assertExtension(element, Variable.class, types);
        }
        Checker.assertExtension(types.asElement(executableElement.getReturnType()), StateTransition.class, types);
        TypeElement containerClass = (TypeElement) operator.getEnclosingElement();
        Checker.assertExtension(containerClass, parent, types);
    }


    void checkSameVariables(UnifierGenerator.Container container) throws GenException {
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
}
