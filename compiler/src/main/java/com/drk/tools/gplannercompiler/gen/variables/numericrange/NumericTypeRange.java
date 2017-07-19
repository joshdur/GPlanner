package com.drk.tools.gplannercompiler.gen.variables.numericrange;

import com.drk.tools.gplannercompiler.gen.variables.support.Extractor;
import com.drk.tools.gplannercore.annotations.variables.NumericRange;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;

class NumericTypeRange {
    private final static String CLASS_NAME = "%sRange";
    private final TypeElement element;
    private final Types types;
    private final NumericRange numericRange;

    NumericTypeRange(Element element, Types types) {
        this.element = (TypeElement) element;
        this.types = types;
        this.numericRange = element.getAnnotation(NumericRange.class);
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

    int getStartValue() {
        return numericRange.start();
    }

    int getEndValue() {
        return numericRange.end();
    }


}
