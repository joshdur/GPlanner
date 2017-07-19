package com.drk.tools.gplannercompiler.gen.context;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.main.BaseUnifier;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class SpecContext {

    private final static String DOMAIN_NAME = "%sContext";

    private final TypeContext typeContext;
    private final Logger logger;

    SpecContext(TypeContext typeContext, Logger logger) {
        this.typeContext = typeContext;
        this.logger = logger;
    }

    String getPackage() {
        return typeContext.getPackage();
    }

    TypeSpec getTypeSpec() throws GenException {
        logger.log(this, "- Building for " + typeContext.getClassName());
        TypeSpec.Builder builder = TypeSpec.classBuilder(String.format(DOMAIN_NAME, typeContext.getClassName()));
        builder.superclass(Context.class);
        builder.addModifiers(Modifier.PUBLIC);
        builder.addMethod(getConstructor());
        builder.addMethod(getConstructorWithBoolean());
        builder.addMethod(getUnifiers());
        addDynamicVariableMethods(builder, typeContext.getCollectionElements());
        return builder.build();
    }

    private MethodSpec getConstructorWithBoolean() throws GenException {
        logger.log(this, "- Building constructor with mapper");
        MethodSpec.Builder builder = MethodSpec.constructorBuilder();
        builder.addModifiers(Modifier.PUBLIC);
        builder.addParameter(Boolean.class, "isDebug");
        builder.addStatement("super(isDebug)");
        addNewInstances(builder, typeContext.getOperatorClasses());
        addVariables(builder, typeContext.getCollectionElements());
        return builder.build();
    }

    private MethodSpec getConstructor() throws GenException {
        logger.log(this, "- Building constructor");
        MethodSpec.Builder builder = MethodSpec.constructorBuilder();
        builder.addModifiers(Modifier.PUBLIC);
        builder.addStatement("this(false)");
        return builder.build();
    }

    private void addNewInstances(MethodSpec.Builder builder, Collection<String> operatorClasses) {
        for (String literal : operatorClasses) {
            builder.addStatement("this.addOperatorToInject(new $L(this))", literal);
        }
    }

    private void addVariables(MethodSpec.Builder builder, Collection<TypeContext.RangeElement> rangeElements) {
        for (TypeContext.RangeElement rangeElement : rangeElements) {
            builder.addStatement("this.addVariable($T.class, new $T())", rangeElement.variableTypeName, rangeElement.rangeTypeName);
        }
    }

    private void addDynamicVariableMethods(TypeSpec.Builder builder, Collection<TypeContext.RangeElement> rangeElements) {
        for (TypeContext.RangeElement rangeElement : rangeElements) {
            if (rangeElement.isDynamic) {
                builder.addMethod(MethodSpec.methodBuilder(rangeElement.variableName)
                        .returns(rangeElement.rangeTypeName)
                        .addStatement("return ($T) injectRangeFromVariableClass($T.class)", rangeElement.rangeTypeName, rangeElement.variableTypeName)
                        .build())
                        .build();
            }
        }

    }

    private MethodSpec getUnifiers() {
        logger.log(this, "- Building getUnifiers method");
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getUnifiers");
        builder.addModifiers(Modifier.PUBLIC);
        builder.addAnnotation(Override.class);
        builder.returns(ParameterizedTypeName.get(List.class, BaseUnifier.class));
        builder.addStatement("$1T<$2T> unifiers = new $3T<$2T>()", List.class, BaseUnifier.class, ArrayList.class);
        List<InstanceData> unifiers = typeContext.getUnifierInstanceData();
        for (InstanceData data : unifiers) {
            builder.addStatement("unifiers.add(new $1T(this))", data.unifier);
        }
        builder.addStatement("return unifiers");
        return builder.build();
    }
}
