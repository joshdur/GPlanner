package com.drk.tools.gplannercompiler.gen.base;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.CompilerFiler;
import com.drk.tools.gplannercompiler.gen.GenException;

import java.util.List;

public class Generator {

    private final String name;
    private final SpecBuilder specBuilder;
    private final Checker checker;
    private final Logger logger;

    public Generator(String name, SpecBuilder specBuilder, Checker checker, Logger logger) {
        this.name = name;
        this.specBuilder = specBuilder;
        this.checker = checker;
        this.logger = logger;
    }

    public void generate(CompilerFiler filer) throws GenException {
        logger.log(this, String.format("Generate %s...", name));
        checker.check();
        logger.log(this, "Building Specs");
        List<? extends Spec> specs = specBuilder.buildSpecs();
        logger.log(this, String.format("Generating %s", name));
        generateLanguage(specs, filer);
    }

    private void generateLanguage(List<? extends Spec> specs, CompilerFiler filer) throws GenException {
        for (Spec spec : specs) {
            filer.writeClass(spec.getPackage(), spec.getTypeSpec());
        }
    }

}
