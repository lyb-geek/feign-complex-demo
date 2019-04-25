package com.github.geek.lyb.feign.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface FieldAlias {

    String value() default "";

    boolean isPojo() default false;

}
