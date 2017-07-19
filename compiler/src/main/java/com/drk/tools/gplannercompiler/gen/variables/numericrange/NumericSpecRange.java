package com.drk.tools.gplannercompiler.gen.variables.numericrange;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercore.core.variables.numeric.NumericVariableRange;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

class NumericSpecRange {

    private final NumericTypeRange type;
    private final Logger logger;

    NumericSpecRange(NumericTypeRange type, Logger logger) {
        this.type = type;
        this.logger = logger;
    }

    String getPackage() {
        return type.getPackage();
    }

    TypeSpec getTypeSpec() {
        return TypeSpec.classBuilder(type.getClassName())
                .addModifiers(Modifier.PUBLIC)
                .superclass(ParameterizedTypeName.get(ClassName.get(NumericVariableRange.class), type.getInstanceTypeName()))
                .addMethod(getConstructor())
                .addMethod(getVariableOfMethod())
                .build();
    }

    private MethodSpec getConstructor(){
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addCode("super($L,$L);", type.getStartValue(), type.getEndValue())
                .build();
    }

    private MethodSpec getVariableOfMethod(){
        return MethodSpec.methodBuilder("variableOf")
                .addModifiers(Modifier.PROTECTED)
                .returns(type.getInstanceTypeName())
                .addParameter(TypeName.INT, "value")
                .addCode("return new $T(value, this);", type.getInstanceTypeName())
                .build();
    }
}
