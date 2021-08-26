package com.ericlam.mc.eldgui.view;

import com.ericlam.mc.eldgui.InventoryTemplate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在 {@link View} 上使用標注，用於指定 template 文件來進行預設渲染。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UseTemplate {

    /**
     *
     * @return template 文件
     */
    String template();

    /**
     *
     * @return 指定文件組類別
     */
    Class<? extends InventoryTemplate> groupResource();

}
