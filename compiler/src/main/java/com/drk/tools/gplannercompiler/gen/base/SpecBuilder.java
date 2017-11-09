package com.drk.tools.gplannercompiler.gen.base;

import com.drk.tools.gplannercompiler.gen.GenException;

import java.util.List;

public interface SpecBuilder {

    List<? extends Spec> buildSpecs() throws GenException;

}
