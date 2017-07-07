package com.drk.tools.gplannercompiler.gen.ranges;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.CompilerFiler;
import com.drk.tools.gplannercompiler.gen.GenException;

import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NumericRangeGenerator {

    private final Set<? extends Element> numericRanges;
    private final Logger logger;
    private final Types types;
    private RangeChecker rangeChecker;

    public NumericRangeGenerator(Set<? extends Element> numericRanges, Logger logger, Types types) {
        this.numericRanges = numericRanges;
        this.logger = logger;
        this.types = types;
        this.rangeChecker = new RangeChecker(logger, types);
    }

    public void generate(CompilerFiler filer) throws GenException {
        logger.log(this, "Generate Numeric Ranges...");
        rangeChecker.checkNumeric(numericRanges);
        logger.log(this, "Building Specs");
        List<NumericSpecRange> specs = buildSpecs();
        logger.log(this, "Generate Unifiers");
        generate(specs, filer);
    }

    private List<NumericSpecRange> buildSpecs() {
        List<NumericSpecRange> specs = new ArrayList<>();
        for (TypeRange type : buildTypes()) {
            specs.add(new NumericSpecRange(type, logger));
        }
        return specs;
    }

    private List<TypeRange> buildTypes() {

        return null;
    }

    private void generate(List<NumericSpecRange> specs, CompilerFiler filer) throws GenException {
        for (NumericSpecRange spec : specs) {
            filer.writeClass(spec.getPackage(), spec.getTypeSpec());
        }
    }

}
