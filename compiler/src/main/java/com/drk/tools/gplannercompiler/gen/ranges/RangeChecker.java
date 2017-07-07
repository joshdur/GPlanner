package com.drk.tools.gplannercompiler.gen.ranges;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercore.core.variables.numeric.NumericVariable;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;
import java.util.Set;

class RangeChecker {

    private final Logger logger;
    private final Types types;

    RangeChecker(Logger logger, Types types) {
        this.logger = logger;
        this.types = types;
    }

    void checkNumeric(Set<? extends Element> numericRanges) throws GenException {
        logger.log(this, "Checking numericRanges");
        for (Element e : numericRanges) {
            checkElement(e);
        }
    }

    private void checkElement(Element element) throws GenException {
        String name = element.getSimpleName().toString();
        if (element.getKind() != ElementKind.CLASS) {
            throw new GenException(name + " must be a class");
        }
        TypeElement typeElement = (TypeElement) element;
        if (typeElement.getSuperclass().toString().equalsIgnoreCase(NumericVariable.class.getCanonicalName()) {
             
        }
    }
}
