package com.drk.tools.gplannercompiler.gen.variables.collectionrange;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercompiler.gen.variables.support.Checker;
import com.drk.tools.gplannercompiler.gen.variables.support.Extractor;
import com.drk.tools.gplannercore.core.variables.collection.CollectionVariable;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Set;

class CollectionRangeChecker {

    private final Logger logger;
    private final Types types;

    CollectionRangeChecker(Logger logger, Types types) {
        this.logger = logger;
        this.types = types;
    }

    void check(Set<? extends Element> collectionRanges) throws GenException {
        logger.log(this, "Checking collectionRanges");
        for (Element e : collectionRanges) {
            checkElement(e);
        }
    }

    private void checkElement(Element element) throws GenException {
        Checker.assertIsPublic(element);
        Checker.assertExtension(element, CollectionVariable.class, types);
        Checker.assertPublicConstructorCount(element, 1);
        Checker.assertConstructorVariableCount(element, 1);
        List<TypeMirror> parameters = Extractor.getParametersOfSuperclass(element);
        Checker.assertConstructorVariableType(element, types, parameters.toArray());
    }
}
