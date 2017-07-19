package com.drk.tools.gplannercompiler.gen.variables.collectionrange;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.CompilerFiler;
import com.drk.tools.gplannercompiler.gen.GenException;

import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.*;

public class CollectionRangeGenerator {

    private final Set<? extends Element> collectionRanges;
    private final Logger logger;
    private final Types types;
    private final CollectionRangeChecker checker;

    public CollectionRangeGenerator(Set<? extends Element> collectionRanges, Logger logger, Types types) {
        this.checker = new CollectionRangeChecker(logger, types);
        this.collectionRanges = collectionRanges;
        this.logger = logger;
        this.types = types;
    }


    public HashMap<String, String> generate(CompilerFiler filer) throws GenException {
        if (collectionRanges.isEmpty()) {
            return new LinkedHashMap<>();
        }
        logger.log(this, "Generate Range Ranges...");
        checker.check(collectionRanges);
        logger.log(this, "Building Specs");
        List<CollectionTypeRange> types = buildTypes();
        List<CollectionSpecRange> specs = buildSpecs(types);
        logger.log(this, "Generating Range ranges");
        generate(specs, filer);
        return mapOfRanges(types);
    }

    private HashMap<String, String> mapOfRanges(List<CollectionTypeRange> ranges) {
        HashMap<String, String> map = new LinkedHashMap<>();
        for (CollectionTypeRange range : ranges) {
            map.put(range.getInstanceCanonicalName(), range.getRangeCanonicalName());
        }
        return map;
    }

    private List<CollectionSpecRange> buildSpecs(List<CollectionTypeRange> types) {
        List<CollectionSpecRange> specs = new ArrayList<>();
        for (CollectionTypeRange type : types) {
            specs.add(new CollectionSpecRange(type, logger));
        }
        return specs;
    }

    private List<CollectionTypeRange> buildTypes() {
        List<CollectionTypeRange> ranges = new ArrayList<>();
        for (Element element : collectionRanges) {
            ranges.add(new CollectionTypeRange(element, types));
        }
        return ranges;
    }

    private void generate(List<CollectionSpecRange> specs, CompilerFiler filer) throws GenException {
        for (CollectionSpecRange spec : specs) {
            filer.writeClass(spec.getPackage(), spec.getTypeSpec());
        }
    }
}
