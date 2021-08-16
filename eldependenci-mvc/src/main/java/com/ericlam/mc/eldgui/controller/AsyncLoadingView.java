package com.ericlam.mc.eldgui.controller;

import com.ericlam.mc.eldgui.view.LoadingView;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AsyncLoadingView {

    Class<? extends LoadingView> value();

}
