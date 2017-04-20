package com.drk.tools.gplannercompiler.gen.unifier;

import javax.lang.model.element.ExecutableElement;

class TypeUnifier {

    private ExecutableElement operator;
    private ExecutableElement systemAction;

    void setOperator(ExecutableElement operator) {
        this.operator = operator;
    }

    void setSystemAction(ExecutableElement systemAction) {
        this.systemAction = systemAction;
    }
}
