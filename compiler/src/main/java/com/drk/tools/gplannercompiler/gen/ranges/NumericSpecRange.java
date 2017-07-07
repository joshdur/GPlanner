package com.drk.tools.gplannercompiler.gen.ranges;

import com.drk.tools.gplannercompiler.Logger;
import com.squareup.javapoet.TypeSpec;

public class NumericSpecRange {
    private String aPackage;
    private TypeSpec typeSpec;

    public NumericSpecRange(TypeRange type, Logger logger) {
    }

    public String getPackage() {
        return aPackage;
    }

    public TypeSpec getTypeSpec() {
        return typeSpec;
    }
}
