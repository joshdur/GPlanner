package com.drk.tools.gplannercompiler.gen.unifier;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.CompilerFiler;
import com.drk.tools.gplannercompiler.gen.GenException;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.*;


public class UnifierGenerator {

    private final Set<? extends Element> operators;
    private final Set<? extends Element> systemActions;
    private final HashMap<String, String> ranges;
    private final Logger logger;
    private final UnifierChecker unifierChecker;

    public UnifierGenerator(Set<? extends Element> operators, Set<? extends Element> systemActions, HashMap<String, String> ranges, Logger logger, Types types) {
        this.operators = operators;
        this.systemActions = systemActions;
        this.logger = logger;
        this.ranges = ranges;
        this.unifierChecker = new UnifierChecker(logger, types);
    }

    public void generate(CompilerFiler filer) throws GenException {
        logger.log(this, "Generate unifiers...");
        unifierChecker.check(operators, systemActions);
        logger.log(this, "Building Specs");
        List<SpecUnifier> specs = buildSpecs();
        logger.log(this, "Generate Unifiers");
        generateUnifiers(specs, filer);
    }

    private void generateUnifiers(List<SpecUnifier> specs, CompilerFiler filer) throws GenException {
        for (SpecUnifier spec : specs) {
            filer.writeClass(spec.getPackage(), spec.getTypeSpec());
        }
    }


    private List<SpecUnifier> buildSpecs() throws GenException {
        List<SpecUnifier> specs = new ArrayList<>();
        for (TypeUnifier type : buildTypes()) {
            specs.add(new SpecUnifier(type, logger));
        }
        return specs;
    }

    private List<TypeUnifier> buildTypes() throws GenException {
        HashMap<String, Container> hashTypes = new LinkedHashMap<>();
        for (Element operator : operators) {
            addToHash(operator, hashTypes, true);
        }
        for (Element action : systemActions) {
            addToHash(action, hashTypes, false);
        }
        List<TypeUnifier> types = new ArrayList<>();
        for (Container container : hashTypes.values()) {
            if (container.hasValidOperator()) {
                unifierChecker.checkSameVariables(container);
                types.add(new TypeUnifier(container.name, container.operator, container.systemAction, container.operatorVariables, ranges));
            }
        }
        return types;
    }

    private void addToHash(Element element, HashMap<String, Container> hash, boolean operator) {
        String name = element.getSimpleName().toString();
        Container container = hash.get(name);
        if (container == null) {
            container = new Container();
            container.name = name;
            hash.put(name, container);
        }
        ExecutableElement executableElement = (ExecutableElement) element;
        if (operator) {
            container.operator = executableElement;
            container.operatorVariables = executableElement.getParameters();
        } else {
            container.systemAction = executableElement;
            container.systemActionVariables = executableElement.getParameters();
        }
    }


    static class Container {
        String name;
        ExecutableElement operator;
        ExecutableElement systemAction;
        List<? extends VariableElement> operatorVariables;
        List<? extends VariableElement> systemActionVariables;

        boolean hasValidOperator() {
            return operator != null && operatorVariables != null;
        }

        boolean hasValidSystemAction(){
            return systemAction != null && systemActionVariables != null;
        }
    }
}
