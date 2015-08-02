package com.aucklanduni.p4p.scalang.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by Taz on 2/08/15.
 */
@Target(ElementType.FIELD)
public @interface NullableField {

    String name() default "";

}
