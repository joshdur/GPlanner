package com.drk.tools.gplannercompiler.gen.variables.collectionrange;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercore.annotations.core.Collection;
import com.drk.tools.gplannercore.core.variables.collection.CollectionVariableRange;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

class CollectionSpecRange {

    private final CollectionTypeRange type;
    private final Logger logger;

    CollectionSpecRange(CollectionTypeRange type, Logger logger) {
        this.type = type;
        this.logger = logger;
    }

    String getPackage() {
        return type.getPackage();
    }

    TypeSpec getTypeSpec() throws GenException {
        return TypeSpec.classBuilder(type.getClassName())
                .addAnnotation(Collection.class)
                .addModifiers(Modifier.PUBLIC)
                .superclass(ParameterizedTypeName.get(ClassName.get(CollectionVariableRange.class), type.getInstanceTypeName()))
                .addMethod(getConstructor())
                .build();
    }

    private MethodSpec getConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .build();
    }

}
