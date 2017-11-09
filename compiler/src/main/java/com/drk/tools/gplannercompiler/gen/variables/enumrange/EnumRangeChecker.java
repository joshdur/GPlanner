package com.drk.tools.gplannercompiler.gen.variables.enumrange;

import com.drk.tools.gplannercompiler.Logger;
import com.drk.tools.gplannercompiler.gen.GenException;
import com.drk.tools.gplannercompiler.gen.base.Checker;
import com.drk.tools.gplannercompiler.gen.support.CheckerSupport;
import com.drk.tools.gplannercore.annotations.variables.EnumRange;
import com.drk.tools.gplannercore.core.variables.enumvars.EnumVariable;

import javax.lang.model.element.Element;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Set;

public class EnumRangeChecker implements Checker {

    private final Set<? extends Element> enumRanges;
    private final Logger logger;
    private final Types types;

    public EnumRangeChecker(Set<? extends Element> enumRanges, Logger logger, Types types) {
        this.enumRanges = enumRanges;
        this.logger = logger;
        this.types = types;
    }

    @Override
    public void check() throws GenException {
        logger.log(this, "Checking enumRanges");
        for (Element e : enumRanges) {
            checkElement(e);
        }
    }

    private void checkElement(Element element) throws GenException {
        checkEnumRange(element);
        CheckerSupport.assertIsPublic(element);
        CheckerSupport.assertExtension(element, EnumVariable.class, types);
        CheckerSupport.assertPublicConstructorCount(element, 1);

        CheckerSupport.assertConstructorVariableCount(element, 1);
        CheckerSupport.assertConstructorVariableType(element, types, Enum.class);
    }

    private void checkEnumRange(Element element) throws GenException {
        String name = element.getSimpleName().toString();
        EnumRange enumRange = element.getAnnotation(EnumRange.class);
        try {
            Class eClass = enumRange.enumClass();
            logger.log(this, "class--   " + eClass.getCanonicalName());
            if (!eClass.getSuperclass().equals(Enum.class)) {
                throw new GenException(name + " enumClass should be an Enum");
            }
        } catch (MirroredTypeException e) {
            TypeMirror typeMirror = e.getTypeMirror();
            logger.log(this, "typeMirror--   " + typeMirror.toString());

            List<? extends TypeMirror> supertypes = types.directSupertypes(typeMirror);
            TypeMirror supertype = supertypes.get(0);
            logger.log(this, "syperTypeMirror--   " + supertype.toString());

            if (!supertype.toString().contains(Enum.class.getCanonicalName())) {
                throw new GenException(name + " enumClass should be an Enum");
            }
        }
    }
}
