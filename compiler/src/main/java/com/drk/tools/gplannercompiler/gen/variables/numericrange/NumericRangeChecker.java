package com.drk.tools.gplannercompiler.gen.variables.numericrange;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercompiler.gen.variables.support.Checker;
import com.drk.tools.gplannercore.annotations.variables.NumericRange;
import com.drk.tools.gplannercore.core.variables.numeric.NumericVariable;
import com.drk.tools.gplannercore.core.variables.numeric.NumericVariableRange;

import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.Set;

class NumericRangeChecker {

    private final Logger logger;
    private final Types types;

    NumericRangeChecker(Logger logger, Types types) {
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
        NumericRange numericRange = element.getAnnotation(NumericRange.class);
        if(numericRange.start() > numericRange.end()){
            throw new GenException(element.getSimpleName().toString() + " - Start should be lower than end");
        }

        Checker.assertIsPublic(element);
        Checker.assertExtension(element, NumericVariable.class, types);
        Checker.assertPublicConstructorCount(element, 1);
        Checker.assertConstructorVariableCount(element, 2);
        Checker.assertConstructorVariableType(element, types, Integer.class, NumericVariableRange.class);
    }
}
