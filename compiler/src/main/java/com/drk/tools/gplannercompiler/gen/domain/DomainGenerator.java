package com.drk.tools.gplannercompiler.gen.domain;

import com.drk.tools.gplannercompiler.Logger;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.Set;

public class DomainGenerator {

    private final Set<? extends Element> unifiers;
    private final Types types;
    private final Logger logger;

    public DomainGenerator(Set<? extends Element> unifiers, Types types, Logger logger) {
        this.unifiers = unifiers;
        this.types = types;
        this.logger = logger;
    }

    public void generate(Filer filer) {

    }
}
