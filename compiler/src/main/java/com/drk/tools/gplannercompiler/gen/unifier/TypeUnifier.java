package com.drk.tools.gplannercompiler.gen.unifier;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercore.core.main.SystemActions;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class TypeUnifier {

    private final String name;
    private final ExecutableElement operator;
    private final ExecutableElement systemAction;
    private final List<? extends VariableElement> variables;
    private final HashMap<String, String> ranges;

    TypeUnifier(String name, ExecutableElement operator, ExecutableElement systemAction, List<? extends VariableElement> operatorVariables, HashMap<String, String> ranges) {
        this.name = name;
        this.operator = operator;
        this.systemAction = systemAction;
        this.variables = operatorVariables;
        this.ranges = ranges;
    }

    String getPackageName() {
        TypeElement typeElement = getTypeElement(operator);
        String qualifiedName = typeElement.getQualifiedName().toString();
        return qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
    }

    String getDomainName() {
        TypeElement typeElement = getTypeElement(operator);
        return typeElement.getSimpleName().toString();
    }

    List<TypeName> getVariables() {
        List<TypeName> typeNames = new ArrayList<>();
        for (VariableElement variable : variables) {
            typeNames.add(TypeName.get(variable.asType()));
        }
        return typeNames;
    }

    List<String> getRanges() {
        List<String> typeNames = new ArrayList<>();
        for (VariableElement variable : variables) {
            String canonicalNameRange = ranges.get(variable.asType().toString());
            typeNames.add(canonicalNameRange);
        }
        return typeNames;
    }

    String getHash() {
        return "";
    }

    String getOperatorName() {
        return operator.getSimpleName().toString();
    }

    String getClassName() {
        String operatorName = getOperatorName();
        return operatorName.substring(0, 1).toUpperCase() + operatorName.substring(1);
    }

    TypeName getOperatorsType() {
        TypeElement typeElement = getTypeElement(operator);
        return TypeName.get(typeElement.asType());

    }

    boolean existsSystemAction() {
        return systemAction != null;
    }

    TypeName getSystemActionsType() {
        if (existsSystemAction()) {
            TypeElement typeElement = getTypeElement(systemAction);
            return TypeName.get(typeElement.asType());
        } else {
            return TypeName.get(SystemActions.class);
        }
    }

    private TypeElement getTypeElement(ExecutableElement element) {
        return (TypeElement) element.getEnclosingElement();
    }
}
