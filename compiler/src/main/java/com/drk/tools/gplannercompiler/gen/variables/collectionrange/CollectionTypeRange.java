package com.drk.tools.gplannercompiler.gen.variables.collectionrange;

import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercompiler.gen.variables.support.Extractor;
import com.drk.tools.gplannercore.annotations.variables.CollectionRange;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.List;

class CollectionTypeRange {
    private final static String CLASS_NAME = "%sRange";
    private final TypeElement element;
    private final Types types;

    CollectionTypeRange(Element element, Types types) {
        this.element = (TypeElement) element;
        this.types = types;
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

    TypeName getInstanceTypeName() throws GenException {
        return TypeName.get(element.asType());
    }

    String getInstanceCanonicalName() {
        return element.asType().toString();
    }


}
