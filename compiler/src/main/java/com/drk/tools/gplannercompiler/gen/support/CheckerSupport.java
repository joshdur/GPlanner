package com.drk.tools.gplannercompiler.gen.support;

import com.drk.tools.gplannercompiler.gen.GenException;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.Set;

public class CheckerSupport {


    public static void assertIsPublic(Element element) throws GenException {
        String name = element.getSimpleName().toString();
        if (!element.getModifiers().contains(Modifier.PUBLIC)) {
            throw new GenException(name + " must be public");
        }
    }

    public static void assertHasNotModifiers(Element element, Modifier... modifiers) throws GenException {
        String name = element.getSimpleName().toString();
        Set<Modifier> modifierSet = element.getModifiers();
        for (Modifier modifier : modifiers) {
            if (modifierSet.contains(modifier)) {
                throw new GenException(String.format("%s expected not contains %s but it does", name, modifier.toString()));
            }
        }
    }

    public static void assertIsEnum(Element element) throws GenException {
        assertIs(element, ElementKind.ENUM);
    }

    public static void assertIsClass(Element element) throws GenException {
        assertIs(element, ElementKind.CLASS);
    }

    public static void assertIsConstructor(Element element) throws GenException {
        assertIs(element, ElementKind.CONSTRUCTOR);
    }

    public static void assertIsMethod(Element element) throws GenException {
        assertIs(element, ElementKind.METHOD);
    }

    private static void assertIs(Element element, ElementKind elementKind) throws GenException {
        String name = element.getSimpleName().toString();
        if (element.getKind() != elementKind) {
            throw new GenException(String.format("%s must be a %s", name, elementKind.toString().toLowerCase()));
        }
    }

    public static void assertExtension(Element element, Class<?> tClass, Types types) throws GenException {
        assertExtension(element, tClass.getCanonicalName(), types);
    }

    public static void assertExtension(Element element, TypeMirror typeMirror, Types types) throws GenException {
        assertExtension(element, typeMirror.toString(), types);
    }

    public static void assertExtension(Element element, String canonicalName, Types types) throws GenException {
        TypeElement typeElement = (TypeElement) (element.getKind() == ElementKind.CLASS ? element : types.asElement(element.asType()));
        String name = element.getSimpleName().toString();
        TypeMirror superclass = typeElement.asType();
        while (!superclass.toString().contains(canonicalName) && !superclass.toString().contains(Object.class.getCanonicalName())) {
            TypeElement elementSuperClass = (TypeElement) types.asElement(superclass);
            superclass = elementSuperClass.getSuperclass();
        }

        if (!superclass.toString().contains(canonicalName)) {
            throw new GenException(name + " must extends " + canonicalName.toLowerCase());
        }
    }

    public static void assertPublicConstructorCount(Element element, int count) throws GenException {
        assertIsClass(element);
        TypeElement typeElement = (TypeElement) element;
        String name = typeElement.getSimpleName().toString();
        int constructorCount = 0;
        for (Element enclosed : typeElement.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
                constructorCount++;
                CheckerSupport.assertIsPublic(enclosed);
            }
        }
        if (constructorCount != count) {
            throw new GenException(String.format("%s must have %d constructors but have %d", name, count, constructorCount));
        }
    }

    public static ExecutableElement getConstructorFromTypeElement(Element element) throws GenException {
        assertIsClass(element);
        TypeElement typeElement = (TypeElement) element;
        String name = element.getSimpleName().toString();
        for (Element enclosed : typeElement.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
                return (ExecutableElement) enclosed;
            }
        }
        throw new GenException(name + " no contains constructor");
    }

    public static void assertConstructorVariableCount(Element element, int variableCount) throws GenException {
        ExecutableElement constructor = getConstructorFromTypeElement(element);
        String name = constructor.getSimpleName().toString();
        int size = constructor.getParameters().size();
        if (size != variableCount) {
            throw new GenException(String.format("%s must have %d paramerters but have %d", name, variableCount, size));
        }
    }


    public static void assertConstructorVariableType(Element element, Types types, Object... typeKindOrClasses) throws GenException {
        ExecutableElement constructor = getConstructorFromTypeElement(element);
        String name = constructor.getSimpleName().toString();
        int size = constructor.getParameters().size();
        if (size != typeKindOrClasses.length) {
            throw new GenException(String.format("%s has %d parameters but want to check %d", name, size, typeKindOrClasses.length));
        }
        for (int i = 0; i < size; i++) {
            VariableElement variableElement = constructor.getParameters().get(i);
            Object typeKindOrClass = typeKindOrClasses[i];
            if (typeKindOrClass instanceof TypeKind) {
                assertVariableType(variableElement, (TypeKind) typeKindOrClass);
            } else if (typeKindOrClass instanceof Class) {
                assertExtension(variableElement, (Class) typeKindOrClass, types);
            } else if (typeKindOrClass instanceof String) {
                assertExtension(variableElement, (String) typeKindOrClass, types);
            } else if (typeKindOrClass instanceof TypeMirror) {
                assertExtension(variableElement, typeKindOrClass.toString(), types);
            }
        }
    }

    public static void assertVariableType(VariableElement variableElement, TypeKind typeKind) throws GenException {
        String name = variableElement.getSimpleName().toString();
        if (variableElement.asType().getKind() != typeKind) {
            throw new GenException(String.format("%s must be instance of %s but is %s", name, typeKind.toString(), variableElement.asType().toString()));
        }
    }

}
