package com.ericlam.mc.eldgui.exception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用於標注 {@link ExceptionViewHandler }, 表示本處理器只處理特定 controllers 所抛出的異常
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleForControllers {

    /**
     *
     * @return 只處理特定 Controllers 所抛出的異常
     */
    Class<?>[] value();

}
