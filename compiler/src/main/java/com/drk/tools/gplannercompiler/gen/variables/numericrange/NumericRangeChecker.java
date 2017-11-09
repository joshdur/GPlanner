package com.drk.tools.gplannercompiler.gen.variables.numericrange;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercompiler.gen.base.Checker;
import com.drk.tools.gplannercompiler.gen.support.CheckerSupport;
import com.drk.tools.gplannercore.annotations.variables.NumericRange;
import com.drk.tools.gplannercore.core.variables.numeric.NumericVariable;
import com.drk.tools.gplannercore.core.variables.numeric.NumericVariableRange;

import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.Set;

public class NumericRangeChecker implements Checker {

    private final Set<? extends Element> numericRanges;
    private final Logger logger;
    private final Types types;

    public NumericRangeChecker(Set<? extends Element> numericRanges, Logger logger, Types types) {
        this.numericRanges = numericRanges;
        this.logger = logger;
        this.types = types;
    }

    @Override
    public void check() throws GenException {
        logger.log(this, "Checking numericRanges");
        for (Element e : numericRanges) {
            checkElement(e);
        }
    }

    private void checkElement(Element element) throws GenException {
        NumericRange numericRange = element.getAnnotation(NumericRange.class);
        if (numericRange.start() > numericRange.end()) {
            throw new GenException(element.getSimpleName().toString() + " - Start should be lower than end");
        }

        CheckerSupport.assertIsPublic(element);
        CheckerSupport.assertExtension(element, NumericVariable.class, types);
        CheckerSupport.assertPublicConstructorCount(element, 1);
        CheckerSupport.assertConstructorVariableCount(element, 2);
        CheckerSupport.assertConstructorVariableType(element, types, Integer.class, NumericVariableRange.class);
    }
}
