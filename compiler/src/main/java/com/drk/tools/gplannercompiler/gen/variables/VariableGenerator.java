package com.drk.tools.gplannercompiler.gen.variables;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.CompilerFiler;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercompiler.gen.variables.collectionrange.CollectionRangeGenerator;
import com.drk.tools.gplannercompiler.gen.variables.enumrange.EnumRangeGenerator;
import com.drk.tools.gplannercompiler.gen.variables.numericrange.NumericRangeGenerator;

import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class VariableGenerator {

    private final NumericRangeGenerator numericRangeGenerator;
    private final EnumRangeGenerator enumRangeGenerator;
    private final CollectionRangeGenerator collectionRangeGenerator;

    public VariableGenerator(Set<? extends Element> numbers, Set<? extends Element> enums, Set<? extends Element> collections, Logger logger, Types types) {
        this.numericRangeGenerator = new NumericRangeGenerator(numbers, logger, types);
        this.enumRangeGenerator = new EnumRangeGenerator(enums, logger, types);
        this.collectionRangeGenerator = new CollectionRangeGenerator(collections, logger, types);
    }

    public HashMap<String, String> generate(CompilerFiler filer) throws GenException {
        HashMap<String, String> variableRanges = new LinkedHashMap<>();
        variableRanges.putAll(numericRangeGenerator.generate(filer));
        variableRanges.putAll(enumRangeGenerator.generate(filer));
        variableRanges.putAll(collectionRangeGenerator.generate(filer));
        return variableRanges;
    }
}
