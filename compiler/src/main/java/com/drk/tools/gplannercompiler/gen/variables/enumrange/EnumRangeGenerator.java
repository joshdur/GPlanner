package com.drk.tools.gplannercompiler.gen.variables.enumrange;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.CompilerFiler;
import com.drk.tools.gplannercompiler.gen.GenException;

import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.*;

public class EnumRangeGenerator {

    private final Set<? extends Element> enumRanges;
    private final Logger logger;
    private final Types types;
    private final EnumRangeChecker enumRangeChecker;

    public EnumRangeGenerator(Set<? extends Element> enumRanges, Logger logger, Types types) {
        this.enumRangeChecker = new EnumRangeChecker(logger, types);
        this.enumRanges = enumRanges;
        this.logger = logger;
        this.types = types;
    }

    public HashMap<String, String> generate(CompilerFiler filer) throws GenException {
        if (enumRanges.isEmpty()) {
            return new LinkedHashMap<>();
        }
        logger.log(this, "Generate Enum Ranges...");
        enumRangeChecker.checkEnums(enumRanges);
        logger.log(this, "Building Specs");
        List<EnumTypeRange> types = buildTypes();
        List<EnumSpecRange> specs = buildSpecs(types);
        logger.log(this, "Generating Enum ranges");
        generate(specs, filer);
        return mapOfRanges(types);
    }

    private HashMap<String, String> mapOfRanges(List<EnumTypeRange> ranges) {
        HashMap<String, String> map = new LinkedHashMap<>();
        for (EnumTypeRange range : ranges) {
            map.put(range.getInstanceCanonicalName(), range.getRangeCanonicalName());
        }
        return map;
    }

    private List<EnumSpecRange> buildSpecs(List<EnumTypeRange> types) {
        List<EnumSpecRange> specs = new ArrayList<>();
        for (EnumTypeRange type : types) {
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
