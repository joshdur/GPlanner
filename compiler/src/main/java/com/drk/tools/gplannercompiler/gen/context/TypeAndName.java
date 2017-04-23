package com.drk.tools.gplannercompiler.gen.context;

import com.squareup.javapoet.TypeName;

class TypeAndName {

    final TypeName typeName;
    final String name;

    TypeAndName(TypeName typeName, String name) {
        this.typeName = typeName;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TypeAndName){
            TypeAndName other = (TypeAndName) obj;
            return typeName.toString().equals(other.typeName.toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return typeName.toString().hashCode();
    }
}
