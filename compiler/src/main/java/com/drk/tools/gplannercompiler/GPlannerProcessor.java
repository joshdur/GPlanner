package com.drk.tools.gplannercompiler;

import com.drk.tools.gplannercompiler.gen.CompilerFiler;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercompiler.gen.context.ContextGenerator;
import com.drk.tools.gplannercompiler.gen.unifier.UnifierGenerator;
import com.drk.tools.gplannercore.annotations.Operator;
import com.drk.tools.gplannercore.annotations.SystemAction;
import com.drk.tools.gplannercore.annotations.core.Unifier;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;
import java.util.HashSet;
import java.util.Set;

public class GPlannerProcessor extends AbstractProcessor {

    private Types types;
    private CompilerFiler filer;
    private Logger logger;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        types = processingEnv.getTypeUtils();
        filer = new CompilerFiler(processingEnv.getFiler());
        logger = new Logger(processingEnv.getMessager());
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add(Operator.class.getCanonicalName());
        annotations.add(SystemAction.class.getCanonicalName());
        annotations.add(Unifier.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            processOperatorsAndSystemActions(roundEnv);
            processUnifiers(roundEnv);
        } catch (Exception e) {
            logger.error(this, e);
        }
        return true;
    }

    private void processOperatorsAndSystemActions(RoundEnvironment roundEnv) throws GenException {
        Set<? extends Element> operators = roundEnv.getElementsAnnotatedWith(Operator.class);
        Set<? extends Element> actions = roundEnv.getElementsAnnotatedWith(SystemAction.class);
        if(operators.isEmpty()){
            return;
        }
        logger.log(this, "Processing operators and SystemActions");
        UnifierGenerator unifierGenerator = new UnifierGenerator(operators, actions, logger, types);
        unifierGenerator.generate(filer);
    }

    private void processUnifiers(RoundEnvironment roundEnv) throws GenException {
        Set<? extends Element> unifiers = roundEnv.getElementsAnnotatedWith(Unifier.class);
        if(unifiers.isEmpty()){
            return;
        }
        logger.log(this, "Processing Unifiers");
        ContextGenerator contextGenerator = new ContextGenerator(unifiers, types, logger);
        contextGenerator.generate(filer);
    }
}
