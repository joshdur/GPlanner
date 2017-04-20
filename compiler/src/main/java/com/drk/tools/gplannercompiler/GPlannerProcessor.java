package com.drk.tools.gplannercompiler;

import com.drk.tools.gplannercompiler.gen.unifier.UnifierGenerator;
import com.drk.tools.gplannercore.annotations.Operator;
import com.drk.tools.gplannercore.annotations.SystemAction;
import com.drk.tools.gplannercore.annotations.core.Unifier;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
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
    private Filer filer;
    private Logger logger;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        types = processingEnv.getTypeUtils();
        filer = processingEnv.getFiler();
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
            logger.log(this, "Start processing");

        } catch (Exception e) {
            logger.error(this, e);
        }

        return true;
    }

    private void processOperatorsAndSystemActions(RoundEnvironment roundEnv) {
        logger.log(this, "Processing operators and SystemActions");
        Set<? extends Element> operators = roundEnv.getElementsAnnotatedWith(Operator.class);
        Set<? extends Element> actions = roundEnv.getElementsAnnotatedWith(SystemAction.class);
        UnifierGenerator unifierGenerator = new UnifierGenerator(operators, actions, logger);
        unifierGenerator.generate();
    }
}
