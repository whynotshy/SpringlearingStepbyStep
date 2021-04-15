package com.yc.springframework.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: testspring
 * @description:
 * @author: LIN
 * @create: 2021~04~05 11:31
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyBean {

    String[] value() default {};

    String[] name() default {};
}
