package com.xzy.forestSystem.sandhook.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HookMode {
    public static final int AUTO = 0;
    public static final int INLINE = 1;
    public static final int REPLACE = 2;

    int value() default 0;
}
