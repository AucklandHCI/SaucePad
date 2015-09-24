package com.aucklanduni.p4p.scalang.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Taz on 2/08/15.
 *
 * this class defines our own annotation "@nullableField" Which allows a field in any of the nodes in the AST to be defined
 * as a null, allowing the applciation to continue past it (during inspection) without throwing a nullpointer exception.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NullableField {

    String name() default "";

}
