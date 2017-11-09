package com.drk.tools.gplannercompiler.gen;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.base.Checker;
import com.drk.tools.gplannercompiler.gen.base.Generator;
import com.drk.tools.gplannercompiler.gen.base.SpecBuilder;
import com.drk.tools.gplannercompiler.gen.context.ContextBuilder;
import com.drk.tools.gplannercompiler.gen.context.ContextChecker;
import com.drk.tools.gplannercompiler.gen.unifier.UnifierBuilder;
import com.drk.tools.gplannercompiler.gen.unifier.UnifierChecker;
import com.drk.tools.gplannercompiler.gen.variables.collectionrange.CollectionRangeBuilder;
import com.drk.tools.gplannercompiler.gen.variables.collectionrange.CollectionRangeChecker;
import com.drk.tools.gplannercompiler.gen.variables.enumrange.EnumRangeBuilder;
import com.drk.tools.gplannercompiler.gen.variables.enumrange.EnumRangeChecker;
import com.drk.tools.gplannercompiler.gen.variables.numericrange.NumericRangeBuilder;
import com.drk.tools.gplannercompiler.gen.variables.numericrange.NumericRangeChecker;

import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class Generation {

    private final Types types;
    private final Logger logger;
    private final CompilerFiler filer;

    public Generation(Types types, Logger logger, CompilerFiler filer) {
        this.types = types;
        this.logger = logger;
        this.filer = filer;
    }

    public HashMap<String, String> generateVariables(Set<? extends Element> enumRanges, Set<? extends Element> numericRanges, Set<? extends Element> collectionRanges) throws GenException {
        Checker enumChecker = new EnumRangeChecker(enumRanges, logger, types);
        Checker numericChecker = new NumericRangeChecker(numericRanges, logger, types);
        Checker collectionChecker = new CollectionRangeChecker(collectionRanges, logger, types);
        EnumRangeBuilder enumRangeBuilder = new EnumRangeBuilder(enumRanges, logger, types);
        NumericRangeBuilder numericRangeBuilder = new NumericRangeBuilder(numericRanges, logger, types);
        CollectionRangeBuilder collectionRangeBuilder = new CollectionRangeBuilder(collectionRanges, logger, types);
        Generator enumGenerator = new Generator("Enum ranges", enumRangeBuilder, enumChecker, logger);
        Generator numericGenerator = new Generator("Numeric ranges", numericRangeBuilder, numericChecker, logger);
        Generator collectionGenerator = new Generator("Collecion ranges", collectionRangeBuilder, collectionChecker, logger);
        enumGenerator.generate(filer);
        numericGenerator.generate(filer);
        collectionGenerator.generate(filer);

        HashMap<String, String> variableRanges = new LinkedHashMap<>();
        variableRanges.putAll(enumRangeBuilder.mapOfRanges());
        variableRanges.putAll(numericRangeBuilder.mapOfRanges());
        variableRanges.putAll(collectionRangeBuilder.mapOfRanges());
        return variableRanges;
    }

    public void generateContext(Set<? extends Element> unifiers, Set<? extends Element> collections) throws GenException {
        Checker checker = new ContextChecker(unifiers, logger, types);
        SpecBuilder specBuilder = new ContextBuilder(unifiers, collections, types, logger);
        Generator generator = new Generator("Context", specBuilder, checker, logger);
        generator.generate(filer);
    }

    public void generateUnifiers(Set<? extends Element> operators, Set<? extends Element> actions, HashMap<String, String> ranges) throws GenException {
        Checker checker = new UnifierChecker(operators, actions, logger, types);
        SpecBuilder specBuilder = new UnifierBuilder(operators, actions, ranges, logger);
        Generator generator = new Generator("Unifiers", specBuilder, checker, logger);
        generator.generate(filer);
    }
}
