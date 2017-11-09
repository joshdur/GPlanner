package com.drk.tools.gplannercompiler.gen.variables.enumrange;

import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercompiler.gen.support.CheckerSupport;
import com.drk.tools.gplannercompiler.gen.support.Extractor;
import com.drk.tools.gplannercore.annotations.variables.EnumRange;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.List;

class EnumTypeRange {
    private final static String CLASS_NAME = "%sRange";
    private final TypeElement element;
    private final Types types;
    private final EnumRange enumRange;

    EnumTypeRange(Element element, Types types) {
        this.element = (TypeElement) element;
        this.types = types;
        this.enumRange = element.getAnnotation(EnumRange.class);
    }

    String getRangeCanonicalName() {
        return getPackage() + "." + getClassName();
    }


    String getPackage() {
        return Extractor.getPackage(element);
    }

    String getClassName() {
        String className = element.getSimpleName().toString();
        return String.format(CLASS_NAME, className);
    }

    TypeName getInstanceTypeName() {
        return TypeName.get(element.asType());
    }

    String getInstanceCanonicalName() {
        return element.asType().toString();
    }

    List<EnumType> getEnumTypes() throws GenException {
        List<EnumType> enumTypes = new ArrayList<>();
        try {
            Class enumClass = enumRange.enumClass();
            addEnumTypesFrom(enumClass, enumTypes);
        } catch (MirroredTypeException e) {
            TypeMirror typeMirror = e.getTypeMirror();
            addEnumTypesFrom(typeMirror, enumTypes);
        }
        return enumTypes;
    }

    private void addEnumTypesFrom(Class<?> tclass, List<EnumType> enumTypes) {
        Object[] constants = tclass.getEnumConstants();
        for (Object constant : constants) {
            enumTypes.add(new EnumType(TypeName.get(tclass), constant.toString()));
        }

    }

    private void addEnumTypesFrom(TypeMirror typeMirror, List<EnumType> enumTypes) throws GenException {
        Element element = types.asElement(typeMirror);
        CheckerSupport.assertIsEnum(element);
        TypeElement typeElement = (TypeElement) element;
        for (Element enclosed : typeElement.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.ENUM_CONSTANT) {
                enumTypes.add(new EnumType(TypeName.get(typeMirror), enclosed.getSimpleName().toString()));
            }
        }
    }


    TypeName getEnumTypeName() {
        try {
            Class eClass = enumRange.enumClass();
            return TypeName.get(eClass);
        } catch (MirroredTypeException e) {
            TypeMirror typeMirror = e.getTypeMirror();
            return TypeName.get(typeMirror);
        }
    }

    public static class EnumType {

        public final TypeName typeName;
        public final String name;

        public EnumType(TypeName typeName, String name) {
            this.typeName = typeName;
            this.name = name;
        }
    }
}
