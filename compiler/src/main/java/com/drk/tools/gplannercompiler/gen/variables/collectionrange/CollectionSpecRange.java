package com.drk.tools.gplannercompiler.gen.variables.collectionrange;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercompiler.gen.base.Spec;
import com.drk.tools.gplannercore.annotations.core.Range;
import com.drk.tools.gplannercore.core.variables.collection.CollectionVariableRange;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

class CollectionSpecRange implements Spec {

    private final CollectionTypeRange type;
    private final Logger logger;

    CollectionSpecRange(CollectionTypeRange type, Logger logger) {
        this.type = type;
        this.logger = logger;
    }

    public String getPackage() {
        return type.getPackage();
    }

    public TypeSpec getTypeSpec() throws GenException {
        return TypeSpec.classBuilder(type.getClassName())
                .addAnnotation(getAnnotationSpec())
                .addModifiers(Modifier.PUBLIC)
                .superclass(ParameterizedTypeName.get(ClassName.get(CollectionVariableRange.class), type.getInstanceTypeName()))
                .addMethod(getConstructor())
                .build();
    }

    private AnnotationSpec getAnnotationSpec() {
        return AnnotationSpec.builder(Range.class)
                .addMember("isDynamic", "$L", true)
                .build();
    }

    private MethodSpec getConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .build();
    }

}
