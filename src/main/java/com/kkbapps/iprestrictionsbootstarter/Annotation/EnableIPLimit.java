package com.kkbapps.iprestrictionsbootstarter.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableIPLimit {

    /**
     * 周期内访问次数限制
     */
    public long count() default -1L;

    /**
     * 周期内访问间隔限制（毫秒）
     */
    public long interval() default -1L;
}
