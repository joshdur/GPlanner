package com.drk.tools.gplannercompiler.gen.base;

import com.drk.tools.gplannercompiler.gen.GenException;
import com.squareup.javapoet.TypeSpec;

public interface Spec {

    String getPackage();

    TypeSpec getTypeSpec() throws GenException;
}
