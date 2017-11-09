package com.drk.tools.gplannercompiler.gen.variables.numericrange;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.base.Spec;
import com.drk.tools.gplannercore.annotations.core.Range;
import com.drk.tools.gplannercore.core.variables.numeric.NumericVariableRange;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

class NumericSpecRange implements Spec {

    private final NumericTypeRange type;
    private final Logger logger;

    NumericSpecRange(NumericTypeRange type, Logger logger) {
        this.type = type;
        this.logger = logger;
    }

    @Override
    public String getPackage() {
        return type.getPackage();
    }

    @Override
    public TypeSpec getTypeSpec() {
        return TypeSpec.classBuilder(type.getClassName())
                .addAnnotation(getAnnotationSpec())
                .addModifiers(Modifier.PUBLIC)
                .superclass(ParameterizedTypeName.get(ClassName.get(NumericVariableRange.class), type.getInstanceTypeName()))
                .addMethod(getConstructor())
                .addMethod(getVariableOfMethod())
                .build();
    }

    private AnnotationSpec getAnnotationSpec() {
        return AnnotationSpec.builder(Range.class)
                .addMember("isDynamic", "$L", false)
                .build();
    }


    private MethodSpec getConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addCode("super($L,$L);", type.getStartValue(), type.getEndValue())
                .build();
    }

    private MethodSpec getVariableOfMethod() {
        return MethodSpec.methodBuilder("variableOf")
                .addModifiers(Modifier.PROTECTED)
                .returns(type.getInstanceTypeName())
                .addParameter(TypeName.INT, "value")
                .addCode("return new $T(value, this);", type.getInstanceTypeName())
                .build();
    }
}
