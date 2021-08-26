package com.ericlam.mc.eldgui.exception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在 {@link ExceptionViewHandler} 中新增處理指定異常的方法時標注使用。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleException {

    /**
     *
     * @return 指定的異常,可多過一個
     */
    Class<? extends Exception>[] value();

}
