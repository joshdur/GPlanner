package com.drk.tools.gplannercore.annotations.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface Unifier {

    String from() default "";

    String hash() default "";

    String operator();

    Class[] variables() default {};
}
