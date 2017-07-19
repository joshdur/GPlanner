package com.drk.tools.gplannercompiler.gen.context;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.CompilerFiler;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercore.annotations.core.Unifier;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;
import java.util.*;

public class ContextGenerator {

    private final Set<? extends Element> unifiers;
    private final Set<? extends Element> collections;
    private final Types types;
    private final Logger logger;
    private final ContextChecker contextChecker;

    public ContextGenerator(Set<? extends Element> unifiers, Set<? extends Element> collections, Types types, Logger logger) {
        this.unifiers = unifiers;
        this.collections = collections;
        this.types = types;
        this.logger = logger;
        this.contextChecker = new ContextChecker(logger, types);
    }

    public void generate(CompilerFiler filer) throws GenException {
        logger.log(this, "Generate domains...");
        contextChecker.check(unifiers);
        contextChecker.checkCollections(collections);
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
        HashMap<String, Set<TypeElement>> hashUnifiers = new LinkedHashMap<>();
        for (Element element : unifiers) {
            addToHashMap(hashUnifiers, element);
        }
        List<TypeContext> typeContexts = new ArrayList<>();
        for (Map.Entry<String, Set<TypeElement>> entry : hashUnifiers.entrySet()) {
            TypeContext typeContext = new TypeContext(entry.getKey(), entry.getValue(), collections, types);
            typeContexts.add(typeContext);
        }
        return typeContexts;
    }

    private void addToHashMap(HashMap<String, Set<TypeElement>> hashUnifiers, Element element) {
        TypeElement typeElement = (TypeElement) element;
        Unifier unifier = element.getAnnotation(Unifier.class);
        String domain = unifier.from();
        Set<TypeElement> typeElements = hashUnifiers.get(domain);
        if (typeElements == null) {
            typeElements = new HashSet<>();
            hashUnifiers.put(domain, typeElements);
        }
        typeElements.add(typeElement);
    }
}
