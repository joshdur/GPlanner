package com.drk.tools.gplannercompiler.gen.variables.enumrange;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercore.annotations.core.Range;
import com.drk.tools.gplannercore.core.variables.enumvars.EnumVariableRange;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

class EnumSpecRange {

    private final EnumTypeRange type;
    private final Logger logger;

    EnumSpecRange(EnumTypeRange type, Logger logger) {
        this.type = type;
        this.logger = logger;
    }

    String getPackage() {
        return type.getPackage();
    }

    TypeSpec getTypeSpec() throws GenException {
        TypeSpec.Builder builder = TypeSpec.classBuilder(type.getClassName())
                .addAnnotation(getAnnotationSpec())
                .addModifiers(Modifier.PUBLIC)
                .superclass(ParameterizedTypeName.get(ClassName.get(EnumVariableRange.class), type.getInstanceTypeName()))
                .addMethod(getConstructor())
                .addMethod(getVariableOfMethod());

        for(EnumTypeRange.EnumType enumType : type.getEnumTypes()) {
            builder.addField(getFieldSpec(enumType));
        }
        return builder.build();
    }

    private AnnotationSpec getAnnotationSpec(){
        return AnnotationSpec.builder(Range.class)
                .addMember("isDynamic", "$L", false)
                .build();
    }

    private FieldSpec getFieldSpec(EnumTypeRange.EnumType enumType){
        return FieldSpec.builder(type.getInstanceTypeName(), enumType.name, Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC)
                .initializer("new $T($T.$L)", type.getInstanceTypeName(), enumType.typeName, enumType.name)
                .build();
    }

    private MethodSpec getConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addCode("super($T.values());", type.getEnumTypeName())
                .build();
    }

    private MethodSpec getVariableOfMethod() {
        return MethodSpec.methodBuilder("variableOf")
                .addModifiers(Modifier.PROTECTED)
                .returns(type.getInstanceTypeName())
                .addParameter(Enum.class, "value")
                .addCode("return new $T(value);", type.getInstanceTypeName())
                .build();
    }
}
