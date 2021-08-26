package com.ericlam.mc.eldgui.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在 {@link View} 上使用標注，用於直接指定預設渲染物件而不經過 template 文件
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewDescriptor {

    /**
     *
     * @return 界面名稱
     */
    String name();

    /**
     *
     * @return 界面行數
     */
    int rows();

    /**
     *
     * @return pattern 分佈
     */
    String[] patterns();

    /**
     *
     * @return 取消點擊事件的 pattern
     */
    char[] cancelMove() default {};

}
