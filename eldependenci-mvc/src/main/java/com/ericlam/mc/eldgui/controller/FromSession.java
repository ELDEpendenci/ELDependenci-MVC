package com.ericlam.mc.eldgui.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface FromSession {

    /**
     * 從 UISession 提取該 key 的數值
     * @return 指定 key
     */
    String value();

    /**
     * 是否使用 pollAttribute 方法
     * @return 是否使用
     */
    boolean poll() default false;

}
