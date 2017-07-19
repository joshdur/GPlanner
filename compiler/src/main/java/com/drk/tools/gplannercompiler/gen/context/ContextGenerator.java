package com.drk.tools.gplannercompiler.gen.context;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.CompilerFiler;
import com.drk.tools.gplannercompiler.gen.GenException;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;
import java.util.*;

public class ContextGenerator {

    private final Set<? extends Element> unifiers;
    private final Set<? extends Element> ranges;
    private final Types types;
    private final Logger logger;
    private final ContextChecker contextChecker;

    public ContextGenerator(Set<? extends Element> unifiers, Set<? extends Element> ranges, Types types, Logger logger) {
        this.unifiers = unifiers;
        this.ranges = ranges;
        this.types = types;
        this.logger = logger;
        this.contextChecker = new ContextChecker(logger, types);
    }

    public void generate(CompilerFiler filer) throws GenException {
        logger.log(this, "Generate domains...");
        contextChecker.check(unifiers);
        contextChecker.checkCollections(ranges);
        logger.log(this, "Building Specs");
        List<SpecContext> specs = buildSpecs();
        logger.log(this, "Start generating domains");
        generateDomains(specs, filer);
    }

    private void generateDomains(List<SpecContext> specs, CompilerFiler filer) throws GenException {
        for (SpecContext spec : specs) {
            filer.writeClass(spec.getPackage(), spec.getTypeSpec());
        }
    }

    private List<SpecContext> buildSpecs() {
        List<SpecContext> specs = new ArrayList<>();
        for (TypeContext typeContext : buildTypes()) {
            specs.add(new SpecContext(typeContext, logger));
        }
        return specs;
    }

    private List<TypeContext> buildTypes() {
        logger.log(this, "Building Types for Specs");
        Set<TypeElement> typeUnifiers = new HashSet<>();
        for (Element element : unifiers) {
            typeUnifiers.add((TypeElement) element);
        }
        return Collections.singletonList(new TypeContext("default", typeUnifiers, ranges, types));
    }

}
