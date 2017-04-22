package com.drk.tools.gplannercompiler.gen;

import com.drk.tools.gplannercompiler.GPlannerProcessor;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.Generated;
import javax.annotation.processing.Filer;
import java.io.IOException;

public class CompilerFiler {

    private final Filer filer;

    public CompilerFiler(Filer filer) {
        this.filer = filer;
    }

    public void writeClass(String packageName, TypeSpec typeSpec) throws GenException {
        try {
            TypeSpec withAnnotation = addCompilerAnnotation(typeSpec);
            JavaFile file = JavaFile.builder(packageName, withAnnotation)
                    .skipJavaLangImports(true)
                    .build();
            file.writeTo(filer);
        } catch (IOException e) {
            throw new GenException(e);
        }
    }

    private TypeSpec addCompilerAnnotation(TypeSpec typeSpec) {
        AnnotationSpec annotation = AnnotationSpec.builder(Generated.class)
                .addMember("value", "$S", GPlannerProcessor.class.getCanonicalName())
                .build();
        return typeSpec.toBuilder()
                .addAnnotation(annotation)
                .build();
    }
}
