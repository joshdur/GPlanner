package com.drk.tools.gplannercompiler.gen.variables.collectionrange;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.CompilerFiler;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercompiler.gen.base.Spec;
import com.drk.tools.gplannercompiler.gen.base.SpecBuilder;

import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.*;

public class CollectionRangeBuilder implements SpecBuilder {

    private final Set<? extends Element> collectionRanges;
    private final Logger logger;
    private final Types types;

    public CollectionRangeBuilder(Set<? extends Element> collectionRanges, Logger logger, Types types) {
        this.collectionRanges = collectionRanges;
        this.logger = logger;
        this.types = types;
    }

    public HashMap<String, String> mapOfRanges() {
        HashMap<String, String> map = new LinkedHashMap<>();
        for (CollectionTypeRange range : buildTypes()) {
            map.put(range.getInstanceCanonicalName(), range.getRangeCanonicalName());
        }
        return map;
    }

    @Override
    public List<? extends Spec> buildSpecs() {
        List<CollectionSpecRange> specs = new ArrayList<>();
        for (CollectionTypeRange type : buildTypes()) {
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
