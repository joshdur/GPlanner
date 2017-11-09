package com.drk.tools.gplannercompiler.gen.context;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercompiler.gen.base.Checker;
import com.drk.tools.gplannercompiler.gen.support.CheckerSupport;
import com.drk.tools.gplannercore.core.Context;
import com.drk.tools.gplannercore.core.variables.collection.CollectionVariableRange;
import com.drk.tools.gplannercore.core.variables.enumvars.EnumVariableRange;
import com.drk.tools.gplannercore.core.variables.numeric.NumericVariableRange;

import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.Set;

public class ContextChecker implements Checker {

    private final Set<? extends Element> unifiers;
    private final Logger logger;
    private final Types types;

    public ContextChecker(Set<? extends Element> unifiers, Logger logger, Types types) {
        this.unifiers = unifiers;
        this.logger = logger;
        this.types = types;
    }

    @Override
    public void check() throws GenException {
        logger.log(this, "Checking unifier classes");
        for (Element element : unifiers) {
            checkUnifier(element);
        }
    }

    private void checkUnifier(Element element) throws GenException {
        CheckerSupport.assertIsClass(element);
        CheckerSupport.assertPublicConstructorCount(element, 1);
        CheckerSupport.assertConstructorVariableType(element, types, Context.class);
    }

    void checkCollections(Set<? extends Element> collections) throws GenException {
        logger.log(this, "Checking collection classes");
        for (Element element : collections) {
            checkCollection(element);
        }
    }

    private void checkCollection(Element element) throws GenException {
        CheckerSupport.assertIsClass(element);
        try {
            CheckerSupport.assertExtension(element, CollectionVariableRange.class, types);
        } catch (GenException ignored1) {
            try {
                CheckerSupport.assertExtension(element, NumericVariableRange.class, types);
            } catch (GenException ignored2) {
                CheckerSupport.assertExtension(element, EnumVariableRange.class, types);
            }
        }
    }


}
