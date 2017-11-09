package com.drk.tools.gplannercompiler.gen.variables.collectionrange;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercompiler.gen.base.Checker;
import com.drk.tools.gplannercompiler.gen.support.CheckerSupport;
import com.drk.tools.gplannercompiler.gen.support.Extractor;
import com.drk.tools.gplannercore.core.variables.collection.CollectionVariable;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Set;

public class CollectionRangeChecker implements Checker {

    private final Set<? extends Element> collectionRanges;
    private final Logger logger;
    private final Types types;

    public CollectionRangeChecker(Set<? extends Element> collectionRanges, Logger logger, Types types) {
        this.collectionRanges = collectionRanges;
        this.logger = logger;
        this.types = types;
    }

    @Override
    public void check() throws GenException {
        logger.log(this, "Checking collectionRanges");
        for (Element e : collectionRanges) {
            checkElement(e);
        }
    }

    private void checkElement(Element element) throws GenException {
        CheckerSupport.assertIsPublic(element);
        CheckerSupport.assertExtension(element, CollectionVariable.class, types);
        CheckerSupport.assertPublicConstructorCount(element, 1);
        CheckerSupport.assertConstructorVariableCount(element, 1);
        List<TypeMirror> parameters = Extractor.getParametersOfSuperclass(element);
        CheckerSupport.assertConstructorVariableType(element, types, parameters.toArray());
    }
}
