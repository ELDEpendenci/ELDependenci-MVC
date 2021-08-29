package com.ericlam.mc.eldgui.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 獲取指定 Pattern 的 所有數值，必須需要使用 {@code Map<String, Object>} 來裝載。
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface MapAttribute {

    /**
     *
     * @return 指定 pattern
     */
    char value();
}
