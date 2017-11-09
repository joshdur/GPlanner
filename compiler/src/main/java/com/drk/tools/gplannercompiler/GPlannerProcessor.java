package com.drk.tools.gplannercompiler;

import com.drk.tools.gplannercompiler.gen.CompilerFiler;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercompiler.gen.Generation;
import com.drk.tools.gplannercore.annotations.Operator;
import com.drk.tools.gplannercore.annotations.SystemAction;
import com.drk.tools.gplannercore.annotations.core.Range;
import com.drk.tools.gplannercore.annotations.core.Unifier;
import com.drk.tools.gplannercore.annotations.variables.CollectionRange;
import com.drk.tools.gplannercore.annotations.variables.EnumRange;
import com.drk.tools.gplannercore.annotations.variables.NumericRange;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class GPlannerProcessor extends AbstractProcessor {

    private Logger logger;
    private Generation generation;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        logger = new Logger(processingEnv.getMessager());
        generation = new Generation(processingEnv.getTypeUtils(), logger, new CompilerFiler(processingEnv.getFiler()));
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add(NumericRange.class.getCanonicalName());
        annotations.add(EnumRange.class.getCanonicalName());
        annotations.add(CollectionRange.class.getCanonicalName());

        annotations.add(Operator.class.getCanonicalName());
        annotations.add(SystemAction.class.getCanonicalName());

        annotations.add(Unifier.class.getCanonicalName());
        annotations.add(Range.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            processOperatorsAndSystemActions(roundEnv, processVariables(roundEnv));
            processUnifiers(roundEnv);
        } catch (Exception e) {
            logger.error(this, e);
        }
        return true;
    }

    private HashMap<String, String> processVariables(RoundEnvironment roundEnv) throws GenException {
        Set<? extends Element> numbers = roundEnv.getElementsAnnotatedWith(NumericRange.class);
        Set<? extends Element> enums = roundEnv.getElementsAnnotatedWith(EnumRange.class);
        Set<? extends Element> collections = roundEnv.getElementsAnnotatedWith(CollectionRange.class);
        if (numbers.isEmpty() && enums.isEmpty() && collections.isEmpty()) {
            return new LinkedHashMap<>();
        }
        return generation.generateVariables(enums, numbers, collections);
    }

    private void processOperatorsAndSystemActions(RoundEnvironment roundEnv, HashMap<String, String> ranges) throws GenException {
        Set<? extends Element> operators = roundEnv.getElementsAnnotatedWith(Operator.class);
        Set<? extends Element> actions = roundEnv.getElementsAnnotatedWith(SystemAction.class);
        if (operators.isEmpty()) {
            return;
        }
        generation.generateUnifiers(operators, actions, ranges);
    }

    private void processUnifiers(RoundEnvironment roundEnv) throws GenException {
        Set<? extends Element> unifiers = roundEnv.getElementsAnnotatedWith(Unifier.class);
        Set<? extends Element> collections = roundEnv.getElementsAnnotatedWith(Range.class);
        if (unifiers.isEmpty()) {
            return;
        }
        generation.generateContext(unifiers, collections);
    }
}
