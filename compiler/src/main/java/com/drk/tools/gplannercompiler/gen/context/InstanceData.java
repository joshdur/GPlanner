package com.drk.tools.gplannercompiler.gen.context;

import com.squareup.javapoet.TypeName;

class InstanceData {

    final TypeName unifier;
    final String operatorsName;
    final String systemActionsName;

    InstanceData(TypeName unifier, String operatorsName, String systemActionsName) {
        this.unifier = unifier;
        this.operatorsName = operatorsName;
        this.systemActionsName = systemActionsName;
    }

    boolean hasSystemActions() {
        return systemActionsName != null;
    }
}
