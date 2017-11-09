package com.drk.tools.gplannercompiler.gen.context;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.base.Spec;
import com.drk.tools.gplannercompiler.gen.base.SpecBuilder;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;
import java.util.*;

public class ContextBuilder implements SpecBuilder {

    private final Set<? extends Element> unifiers;
    private final Set<? extends Element> ranges;
    private final Types types;
    private final Logger logger;

    public ContextBuilder(Set<? extends Element> unifiers, Set<? extends Element> ranges, Types types, Logger logger) {
        this.unifiers = unifiers;
        this.ranges = ranges;
        this.types = types;
        this.logger = logger;
    }

    @Override
    public List<? extends Spec> buildSpecs() {
        List<Spec> specs = new ArrayList<>();
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
