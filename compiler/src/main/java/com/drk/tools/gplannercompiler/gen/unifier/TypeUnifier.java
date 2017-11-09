package com.drk.tools.gplannercompiler.gen.unifier;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.*;

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

    String getCommaSeparatedOperatorClasses() {
        Set<String> operatorClasses = new HashSet<>();
        operatorClasses.add(getTypeElement(operator).asType().toString());
        if (systemAction != null) {
            operatorClasses.add(getTypeElement(systemAction).asType().toString());
        }
        if(operatorClasses.isEmpty()){
            return "{}";
        }
        return operatorClasses.toString().replace("[", "{\"").replace(",", "\", \"").replace("]", "\"}");
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

    boolean containsSystemAction(){
        return systemAction != null;
    }

    TypeName getSystemActionType() {
        TypeElement typeElement = getTypeElement(systemAction);
        return TypeName.get(typeElement.asType());

    }

    private TypeElement getTypeElement(ExecutableElement element) {
        return (TypeElement) element.getEnclosingElement();
    }
}
