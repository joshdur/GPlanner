package com.drk.tools.gplannercompiler.gen.context;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercompiler.gen.variables.support.Checker;
import com.drk.tools.gplannercore.core.main.Operators;
import com.drk.tools.gplannercore.core.main.SystemActions;
import com.drk.tools.gplannercore.core.variables.collection.CollectionVariableRange;

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

        try {
            Checker.assertConstructorVariableCount(element, 2);
            Checker.assertConstructorVariableType(element, types, Operators.class, SystemActions.class);
        } catch (GenException e) {
            Checker.assertConstructorVariableCount(element, 1);
            Checker.assertConstructorVariableType(element,types, Operators.class);
        }
    }

    void checkCollections(Set<? extends Element> collections) throws GenException {
        logger.log(this, "Checking collection classes");
        for (Element element : collections) {
            checkCollection(element);
        }
    }

    private void checkCollection(Element element) throws GenException {
        Checker.assertIsClass(element);
        Checker.assertExtension(element, CollectionVariableRange.class, types);
    }




}
