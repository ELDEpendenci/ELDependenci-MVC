package com.ericlam.mc.eldgui.controller;

import com.ericlam.mc.eldgui.model.Model;
import com.ericlam.mc.eldgui.view.View_Legacy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Deprecated
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ControllerForView {

    Class<? extends View_Legacy<? extends Model>> value();
}
