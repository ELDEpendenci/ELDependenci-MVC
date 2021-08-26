package com.ericlam.mc.eldgui.event;

import com.ericlam.mc.eldgui.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 點擊事件處理
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClickMapping {

    /**
     *
     * @return 指定界面
     */
    Class<? extends View<?>> view();

    /**
     *
     * @return 指定 pattern
     */
    char pattern();

    /**
     *
     * @return 忽略已取消的事件
     */
    boolean ignoreCancelled() default false;

}
