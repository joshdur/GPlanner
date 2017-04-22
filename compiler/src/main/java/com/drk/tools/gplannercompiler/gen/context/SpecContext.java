package com.drk.tools.gplannercompiler.gen.context;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercore.core.Bundle;
import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.main.BaseUnifier;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
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
        addFields(builder, typeContext.getOperatorsTypes());
        addFields(builder, typeContext.getSystemActionTypes());
        builder.addMethod(getConstructor());
        builder.addMethod(getUnifiers());
        return builder.build();
    }

    private void addFields(TypeSpec.Builder builder, List<TypeAndName> types) {
        logger.log(this, "- Adding Fields");
        for (TypeAndName type : types) {
            builder.addField(type.typeName, type.name);
        }
    }

    private MethodSpec getConstructor() throws GenException {
        logger.log(this, "- Building constructor");
        MethodSpec.Builder builder = MethodSpec.constructorBuilder();
        builder.addParameter(Bundle.class, "bundle");
        builder.addStatement("super(bundle)");
        addNewInstances(builder, typeContext.getOperatorsTypes());
        addNewInstances(builder, typeContext.getSystemActionTypes());
        return builder.build();
    }

    private void addNewInstances(MethodSpec.Builder builder, List<TypeAndName> types) {
        for (TypeAndName type : types) {
            builder.addStatement("this.$1L = new $2T", type.name, type.typeName);
        }
    }

    private MethodSpec getUnifiers() {
        logger.log(this, "- Building getUnifiers method");
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getUnifiers");
        builder.addModifiers(Modifier.PUBLIC);
        builder.addAnnotation(Override.class);
        builder.addStatement("%1T<$2T> unifiers = new %3T<$2T>()", List.class, BaseUnifier.class, ArrayList.class);
        List<InstanceData> unifiers = typeContext.getUnifierInstanceData();
        for (InstanceData data : unifiers) {
            if (data.hasSystemActions()) {
                builder.addStatement("unifiers.add(new $1T($2L, $3L)", data.unifier, data.operatorsName, data.systemActionsName);
            } else {
                builder.addStatement("unifiers.add(new $1T($2L)", data.unifier, data.operatorsName);
            }
        }
        builder.addStatement("return unifiers");
        return builder.build();
    }
}
