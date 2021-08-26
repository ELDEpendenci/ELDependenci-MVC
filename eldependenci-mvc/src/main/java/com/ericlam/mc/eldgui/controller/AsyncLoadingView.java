package com.ericlam.mc.eldgui.controller;

import com.ericlam.mc.eldgui.view.LoadingView;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用指定的異步加載界面。可在方法或類上進行標注
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AsyncLoadingView {

    /**
     *
     * @return 指定的異步加載界面類
     */
    Class<? extends LoadingView> value();

}
