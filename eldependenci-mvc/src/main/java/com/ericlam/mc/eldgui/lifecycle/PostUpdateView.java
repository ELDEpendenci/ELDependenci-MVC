package com.ericlam.mc.eldgui.lifecycle;

import com.ericlam.mc.eldgui.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 界面更新後的操作
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostUpdateView {

    /**
     *
     * @return 指定界面
     */
    Class<? extends View<?>> value();

}
