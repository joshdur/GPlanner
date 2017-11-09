package com.drk.tools.gplannercompiler.gen.context;

import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercompiler.gen.support.Extractor;
import com.drk.tools.gplannercore.annotations.core.Range;
import com.drk.tools.gplannercore.annotations.core.Unifier;
import com.drk.tools.gplannercore.core.main.BaseOperators;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.*;

import static com.drk.tools.gplannercompiler.gen.support.Extractor.getVariableName;

class TypeContext {

    private final String name;
    private final List<TypeElement> unifiers;
    private final Set<? extends Element> collections;
    private final Types types;

    TypeContext(String name, Set<TypeElement> unifiers, Set<? extends Element> collections, Types types) {
        this.name = name;
        this.unifiers = new ArrayList<>(unifiers);
        this.collections = collections;
        this.types = types;

    }

    String getPackage() {
        return Extractor.getPackage(unifiers.get(0));
    }

    String getClassName() {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    Set<String> getOperatorClasses() {
        Set<String> operatorClasses = new HashSet<>();
        for (TypeElement baseUnifier : unifiers) {
            Unifier unifier = baseUnifier.getAnnotation(Unifier.class);
            if (unifier != null) {
                operatorClasses.addAll(Arrays.asList(unifier.operatorClasses()));
            }
        }
        return operatorClasses;
    }

    List<InstanceData> getUnifierInstanceData() {
        List<InstanceData> datas = new ArrayList<>();
        for (TypeElement unifier : unifiers) {
            TypeElement operators = getVariable(unifier, BaseOperators.class);
            String nameOperators = getVariableName(operators);
            InstanceData data = new InstanceData(TypeName.get(unifier.asType()), nameOperators, null);
            datas.add(data);
        }
        return datas;
    }

    List<RangeElement> getCollectionElements() throws GenException {
        List<RangeElement> rangeElements = new ArrayList<>();
        for (Element element : collections) {
            TypeName rangeTypeName = TypeName.get(element.asType());
            List<TypeMirror> parameters = Extractor.getParametersOfSuperclass(element);
            TypeName variableTypeName = TypeName.get(parameters.get(0));
            String name = Extractor.getMethodName("get", element.getSimpleName().toString());
            Range range = element.getAnnotation(Range.class);
            rangeElements.add(new RangeElement(range.isDynamic(), rangeTypeName, variableTypeName, name));
        }
        return rangeElements;
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

    static class RangeElement {
        final boolean isDynamic;
        final TypeName rangeTypeName;
        final TypeName variableTypeName;
        final String variableName;

        RangeElement(boolean isDynamic, TypeName rangeTypeName, TypeName variableTypeName, String variableName) {
            this.isDynamic = isDynamic;
            this.rangeTypeName = rangeTypeName;
            this.variableTypeName = variableTypeName;
            this.variableName = variableName;
        }
    }
}
