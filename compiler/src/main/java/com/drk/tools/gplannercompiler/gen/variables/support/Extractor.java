package com.drk.tools.gplannercompiler.gen.variables.support;

import com.drk.tools.gplannercompiler.gen.GenException;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

public class Extractor {

    public static String getPackage(TypeElement element) {
        String qualifiedName = element.getQualifiedName().toString();
        return qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
    }

    public static List<TypeMirror> getParametersOfSuperclass(Element element) throws GenException {
        Checker.assertIsClass(element);
        TypeElement typeElement = (TypeElement) element;
        DeclaredType declaredType = (DeclaredType) typeElement.getSuperclass();
        List<TypeMirror> parameters = new ArrayList<>();
        parameters.addAll(declaredType.getTypeArguments());
        return parameters;
    }

    public static String getVariableName(Element element) {
        if (element == null) {
            return null;
        }
        String name = element.getSimpleName().toString();
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public static String getMethodName(String... parts) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i == 0) {
                stringBuilder.append(parts[i].toLowerCase());
            } else {
                stringBuilder.append(parts[i].substring(0, 1).toUpperCase()).append(parts[i].substring(1).toLowerCase());
            }
        }
        return stringBuilder.toString();
    }
}
