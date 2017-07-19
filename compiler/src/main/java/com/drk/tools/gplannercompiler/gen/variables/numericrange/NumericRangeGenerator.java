package com.drk.tools.gplannercompiler.gen.variables.numericrange;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.CompilerFiler;
import com.drk.tools.gplannercompiler.gen.GenException;

import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.*;

public class NumericRangeGenerator {

    private final Set<? extends Element> numericRanges;
    private final Logger logger;
    private final Types types;
    private NumericRangeChecker numericRangeChecker;

    public NumericRangeGenerator(Set<? extends Element> numericRanges, Logger logger, Types types) {
        this.numericRanges = numericRanges;
        this.logger = logger;
        this.types = types;
        this.numericRangeChecker = new NumericRangeChecker(logger, types);
    }

    public HashMap<String, String> generate(CompilerFiler filer) throws GenException {
        if(numericRanges.isEmpty()){
            return new LinkedHashMap<>();
        }
        logger.log(this, "Generate Numeric Ranges...");
        numericRangeChecker.checkNumeric(numericRanges);
        logger.log(this, "Building Specs");
        List<NumericTypeRange> types = buildTypes();
        List<NumericSpecRange> specs = buildSpecs(types);
        logger.log(this, "Generating Numeric ranges");
        generate(specs, filer);
        return mapOfRanges(types);
    }

    private HashMap<String, String> mapOfRanges(List<NumericTypeRange> ranges){
        HashMap<String, String> map = new LinkedHashMap<>();
        for(NumericTypeRange range : ranges){
            map.put(range.getInstanceCanonicalName(), range.getRangeCanonicalName());
        }
        return map;
    }

    private List<NumericSpecRange> buildSpecs(List<NumericTypeRange> types) {
        List<NumericSpecRange> specs = new ArrayList<>();
        for (NumericTypeRange type : types) {
            specs.add(new NumericSpecRange(type, logger));
        }
        return specs;
    }

    private List<NumericTypeRange> buildTypes() {
        List<NumericTypeRange> ranges = new ArrayList<>();
        for (Element element : numericRanges) {
            ranges.add(new NumericTypeRange(element, types));
        }
        return ranges;
    }

    private void generate(List<NumericSpecRange> specs, CompilerFiler filer) throws GenException {
        for (NumericSpecRange spec : specs) {
            filer.writeClass(spec.getPackage(), spec.getTypeSpec());
        }
    }

}
