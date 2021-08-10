package com.ericlam.mc.eldgui.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Deprecated
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BaseHandler {

    char[] patterns();

    boolean ignoreCancelled() default false;

    int order() default 1;
}
