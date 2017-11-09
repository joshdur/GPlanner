package com.drk.tools.gplannercompiler.gen.variables.enumrange;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.CompilerFiler;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercompiler.gen.base.Spec;
import com.drk.tools.gplannercompiler.gen.base.SpecBuilder;

import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.*;

public class EnumRangeBuilder implements SpecBuilder {

    private final Set<? extends Element> enumRanges;
    private final Logger logger;
    private final Types types;

    public EnumRangeBuilder(Set<? extends Element> enumRanges, Logger logger, Types types) {
        this.enumRanges = enumRanges;
        this.logger = logger;
        this.types = types;
    }


    public HashMap<String, String> mapOfRanges() {
        HashMap<String, String> map = new LinkedHashMap<>();
        for (EnumTypeRange range : buildTypes()) {
            map.put(range.getInstanceCanonicalName(), range.getRangeCanonicalName());
        }
        return map;
    }

    @Override
    public List<? extends Spec> buildSpecs() {
        List<EnumSpecRange> specs = new ArrayList<>();
        for (EnumTypeRange type : buildTypes()) {
            specs.add(new EnumSpecRange(type, logger));
        }
        return specs;
    }

    private List<EnumTypeRange> buildTypes() {
        List<EnumTypeRange> ranges = new ArrayList<>();
        for (Element element : enumRanges) {
            ranges.add(new EnumTypeRange(element, types));
        }
        return ranges;
    }

    private void generate(List<EnumSpecRange> specs, CompilerFiler filer) throws GenException {
        for (EnumSpecRange spec : specs) {
            filer.writeClass(spec.getPackage(), spec.getTypeSpec());
        }
    }
}
