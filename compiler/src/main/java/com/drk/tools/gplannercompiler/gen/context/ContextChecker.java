package com.drk.tools.gplannercompiler.gen.context;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercompiler.gen.variables.support.Checker;
import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.variables.collection.CollectionVariableRange;
import com.drk.tools.gplannercore.core.variables.enumvars.EnumVariableRange;
import com.drk.tools.gplannercore.core.variables.numeric.NumericVariableRange;

import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.Set;

class ContextChecker {

    private final Logger logger;
    private final Types types;

    ContextChecker(Logger logger, Types types) {
        this.logger = logger;
        this.types = types;
    }


    void check(Set<? extends Element> unifiers) throws GenException {
        logger.log(this, "Checking unifier classes");
        for (Element element : unifiers) {
            checkUnifier(element);
        }
    }

    private void checkUnifier(Element element) throws GenException {
        Checker.assertIsClass(element);
        Checker.assertPublicConstructorCount(element, 1);
        Checker.assertConstructorVariableType(element, types, Context.class);
    }

    void checkCollections(Set<? extends Element> collections) throws GenException {
        logger.log(this, "Checking collection classes");
        for (Element element : collections) {
            checkCollection(element);
        }
    }

    private void checkCollection(Element element) throws GenException {
        Checker.assertIsClass(element);
        try {
            Checker.assertExtension(element, CollectionVariableRange.class, types);
        } catch (GenException ignored1) {
            try {
                Checker.assertExtension(element, NumericVariableRange.class, types);
            } catch (GenException ignored2) {
                Checker.assertExtension(element, EnumVariableRange.class, types);
            }
        }
    }


}
