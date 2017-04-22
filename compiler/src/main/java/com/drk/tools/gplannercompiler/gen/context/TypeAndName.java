package com.drk.tools.gplannercompiler.gen.context;

import com.squareup.javapoet.TypeName;

class TypeAndName {

    final TypeName typeName;
    final String name;

    TypeAndName(TypeName typeName, String name) {
        this.typeName = typeName;
        this.name = name;
    }
}
