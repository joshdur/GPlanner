package com.drk.tools.gplannercompiler.gen.unifier;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.base.Spec;
import com.drk.tools.gplannercore.annotations.core.Unifier;
import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.main.BaseUnifier;
import com.drk.tools.gplannercore.core.main.EmptyTransition;
import com.drk.tools.gplannercore.core.state.StateTransition;
import com.drk.tools.gplannercore.core.variables.Variable;
import com.drk.tools.gplannercore.core.variables.VariableRange;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

class SpecUnifier implements Spec{

    private final static String UNIFIER_NAME = "%s$$Unifier";
    private final static String VARIABLE_NAME = "v";

    private final TypeUnifier typeUnifier;
    private final Logger logger;

    SpecUnifier(TypeUnifier typeUnifier, Logger logger) {
        this.typeUnifier = typeUnifier;
        this.logger = logger;
    }

    @Override
    public String getPackage() {
        return typeUnifier.getPackageName();
    }

    @Override
    public TypeSpec getTypeSpec() {
        logger.log(this, "- Building for " + typeUnifier.getClassName());
        return TypeSpec.classBuilder(String.format(UNIFIER_NAME, typeUnifier.getClassName()))
                .superclass(BaseUnifier.class)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(getUnifierAnnotation())
                .addMethod(getVariables())
                .addMethod(getConstructor())
                .addMethod(getBuild())
                .addMethod(getExecute())
                .build();
    }

    private AnnotationSpec getUnifierAnnotation() {
        logger.log(this, "- Annotation Unifier");
        return AnnotationSpec.builder(Unifier.class)
                .addMember("operatorClasses", "$L", typeUnifier.getCommaSeparatedOperatorClasses())
                .addMember("operator", "$S", typeUnifier.getOperatorName())
                .build();
    }

    private MethodSpec getConstructor() {
        logger.log(this, "- Constructor method");
        MethodSpec.Builder builder = MethodSpec.constructorBuilder();
        builder.addModifiers(Modifier.PUBLIC);
        builder.addParameter(Context.class, "context");
        builder.addStatement("super(context, $S)", typeUnifier.getOperatorName());
        return builder.build();
    }

    private MethodSpec getVariables() {
        logger.log(this, "- getVariables method");
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getVariables");
        builder.addAnnotation(Override.class);
        builder.addModifiers(Modifier.PROTECTED);
        builder.addParameter(Context.class, "context");
        builder.returns(ParameterizedTypeName.get(List.class, VariableRange.class));
        List<TypeName> variables = typeUnifier.getVariables();
        builder.addStatement("$1T<$2T> variableRanges = new $3T<$2T>()", List.class, VariableRange.class, ArrayList.class);
        for (TypeName variable : variables) {
            builder.addStatement("variableRanges.add(context.injectRangeFromVariableClass($T.class))", variable);
        }
        builder.addStatement("return variableRanges");
        return builder.build();
    }

    private MethodSpec getBuild() {
        logger.log(this, "- build method");
        MethodSpec.Builder builder = MethodSpec.methodBuilder("build");
        builder.addAnnotation(Override.class);
        builder.returns(StateTransition.class);
        builder.addModifiers(Modifier.PROTECTED);
        builder.addParameter(ParameterizedTypeName.get(List.class, Variable.class), "variables");
        builder.addStatement("$1T $2L = operatorClass($1T.class)", typeUnifier.getOperatorsType(), "operator");
        recoverAndCallMethodWithVariables(builder, "operator");
        return builder.build();
    }

    private MethodSpec getExecute() {
        logger.log(this, "- execute method");
        MethodSpec.Builder builder = MethodSpec.methodBuilder("execute");
        builder.addAnnotation(Override.class);
        builder.addModifiers(Modifier.PROTECTED);
        builder.addParameter(ParameterizedTypeName.get(List.class, Variable.class), "variables");
        builder.returns(StateTransition.class);
        builder.addException(Throwable.class);
        if(!typeUnifier.containsSystemAction()) {
            builder.addStatement("return new $T()", EmptyTransition.class);
        } else {
            builder.addStatement("$1T $2L = operatorClass($1T.class)", typeUnifier.getSystemActionType(), "system");
            builder.addStatement("if(system == null) return new $T()", EmptyTransition.class);
            recoverAndCallMethodWithVariables(builder, "system");
        }
        return builder.build();
    }

    private void recoverAndCallMethodWithVariables(MethodSpec.Builder builder, String instance) {
        List<TypeName> variables = typeUnifier.getVariables();
        for (int i = 0; i < variables.size(); i++) {
            TypeName variable = variables.get(i);
            builder.addStatement("$1T $2L$3L = ($1T) variables.get($3L)", variable, VARIABLE_NAME, i);
        }
        String commaSeparatedVariables = getCommaSeparated(VARIABLE_NAME, variables.size());
        builder.addStatement("return $L.$L($L)", instance, typeUnifier.getOperatorName(), commaSeparatedVariables);
    }

    private String getCommaSeparated(String name, int size) {
        StringBuilder builder = new StringBuilder();
        if (size <= 0) {
            return "";
        }
        String base = "%s%d";
        builder.append(String.format(base, name, 0));
        for (int i = 1; i < size; i++) {
            builder.append(", ");
            builder.append(String.format(base, name, i));
        }
        return builder.toString();
    }


}
