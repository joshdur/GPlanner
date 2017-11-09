package com.drk.tools.gplannercompiler.gen.variables.numericrange;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.CompilerFiler;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercompiler.gen.base.Spec;
import com.drk.tools.gplannercompiler.gen.base.SpecBuilder;

import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.*;

public class NumericRangeBuilder implements SpecBuilder {

    private final Set<? extends Element> numericRanges;
    private final Logger logger;
    private final Types types;

    public NumericRangeBuilder(Set<? extends Element> numericRanges, Logger logger, Types types) {
        this.numericRanges = numericRanges;
        this.logger = logger;
        this.types = types;
    }

    public HashMap<String, String> mapOfRanges() {
        HashMap<String, String> map = new LinkedHashMap<>();
        for (NumericTypeRange range : buildTypes()) {
            map.put(range.getInstanceCanonicalName(), range.getRangeCanonicalName());
        }
        return map;
    }

    @Override
    public List<? extends Spec> buildSpecs() throws GenException {
        List<Spec> specs = new ArrayList<>();
        for (NumericTypeRange type : buildTypes()) {
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


}
