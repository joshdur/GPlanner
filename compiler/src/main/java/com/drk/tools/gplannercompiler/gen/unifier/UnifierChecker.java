package com.drk.tools.gplannercompiler.gen.unifier;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercompiler.gen.base.Checker;
import com.drk.tools.gplannercompiler.gen.support.CheckerSupport;
import com.drk.tools.gplannercore.core.main.BaseOperators;
import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannercore.core.variables.Variable;

import javax.lang.model.element.*;
import javax.lang.model.util.Types;
import java.util.Set;

public class UnifierChecker implements Checker {

    private final Set<? extends Element> operators;
    private final Set<? extends Element> systemActions;
    private final Logger logger;
    private final Types types;

    public UnifierChecker(Set<? extends Element> operators, Set<? extends Element> systemActions, Logger logger, Types types) {
        this.operators = operators;
        this.systemActions = systemActions;
        this.logger = logger;
        this.types = types;
    }

    @Override
    public void check() throws GenException {
        logger.log(this, "Checking operators class");
        for (Element operator : operators) {
            checkMethod(operator, BaseOperators.class);
        }
        logger.log(this, "Checking systemActions class");
        for (Element action : systemActions) {
            checkMethod(action, BaseOperators.class);
        }
    }

    private <T> void checkMethod(Element operator, Class<T> parent) throws GenException {
        CheckerSupport.assertIsMethod(operator);
        CheckerSupport.assertHasNotModifiers(operator, Modifier.PRIVATE, Modifier.PROTECTED, Modifier.STATIC);
        ExecutableElement executableElement = (ExecutableElement) operator;
        for (Element element : executableElement.getParameters()) {
            CheckerSupport.assertExtension(element, Variable.class, types);
        }
        CheckerSupport.assertExtension(types.asElement(executableElement.getReturnType()), StateTransition.class, types);
        TypeElement containerClass = (TypeElement) operator.getEnclosingElement();
        CheckerSupport.assertExtension(containerClass, parent, types);
    }



}
