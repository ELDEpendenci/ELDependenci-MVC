package com.ericlam.mc.eldgui.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 透過指定 pattern 返回 指定類型的 POJO，系統會自動提取 pattern 内所有組件内的綁定屬性並返回新的 POJO 實例
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModelAttribute {

    /**
     *
     * @return 指定 pattern
     */
    char value();

}
