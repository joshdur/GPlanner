package com.drk.tools.gplannersample.vars;

import com.drk.tools.gplannercore.annotations.variables.CollectionRange;
import com.drk.tools.gplannercore.core.variables.collection.CollectionVariable;

@CollectionRange
public class Stuff extends CollectionVariable<String> {

    public Stuff(String value) {
        super(value);
    }

}
