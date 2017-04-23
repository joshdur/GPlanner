package com.drk.tools.gplannercompiler.gen.context;

import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercore.core.main.Operators;
import com.drk.tools.gplannercore.core.main.SystemActions;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.*;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class TypeContext {

    private final String name;
    private final List<TypeElement> unifiers;
    private final Types types;

    TypeContext(String name, Set<TypeElement> unifiers, Types types) {
        this.name = name;
        this.unifiers = new ArrayList<>(unifiers);
        this.types = types;

    }

    String getPackage() {
        TypeElement typeElement = unifiers.get(0);
        String qualifiedName = typeElement.getQualifiedName().toString();
        return qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
    }

    String getClassName() {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    Set<TypeAndName> getOperatorsTypes() throws GenException {
        Set<TypeAndName> typeAndNames = new HashSet<>();
        for (TypeElement unifier : unifiers) {
            TypeElement operators = getVariable(unifier, Operators.class);
            if (operators == null) {
                String canonical = Operators.class.getCanonicalName();
                String unifierName = getVariableName(unifier);
                throw new GenException(String.format("Not found %s as a parameter of constructor in %s", canonical, unifierName));
            }
            typeAndNames.add(new TypeAndName(TypeName.get(operators.asType()), getVariableName(operators)));
        }
        return typeAndNames;
    }

    Set<TypeAndName> getSystemActionTypes() {
        Set<TypeAndName> typeAndNames = new HashSet<>();
        for (TypeElement unifier : unifiers) {
            TypeElement systemActions = getVariable(unifier, SystemActions.class);
            if (systemActions != null) {
                typeAndNames.add(new TypeAndName(TypeName.get(systemActions.asType()), getVariableName(systemActions)));
            }
        }
        return typeAndNames;
    }

    List<InstanceData> getUnifierInstanceData() {
        List<InstanceData> datas = new ArrayList<>();
        for (TypeElement unifier : unifiers) {
            TypeElement operators = getVariable(unifier, Operators.class);
            TypeElement systemActions = getVariable(unifier, SystemActions.class);
            String nameOperators = getVariableName(operators);
            String nameActions = getVariableName(systemActions);
            InstanceData data = new InstanceData(TypeName.get(unifier.asType()), nameOperators, nameActions);
            datas.add(data);
        }
        return datas;
    }

    private String getVariableName(TypeElement typeElement) {
        if (typeElement == null) {
            return null;
        }
        String name = typeElement.getSimpleName().toString();
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    private <T> TypeElement getVariable(TypeElement typeElement, Class<T> parent) {
        List<? extends Element> elements = typeElement.getEnclosedElements();
        for (Element element : elements) {
            if (element.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement constructor = (ExecutableElement) element;
                List<? extends VariableElement> variables = constructor.getParameters();
                for (VariableElement variableElement : variables) {
                    TypeElement variableType = (TypeElement) types.asElement(variableElement.asType());
                    if (variableType.getSuperclass().toString().contains(parent.getCanonicalName())) {
                        return variableType;
                    }
                }
            }
        }
        return null;
    }
}
