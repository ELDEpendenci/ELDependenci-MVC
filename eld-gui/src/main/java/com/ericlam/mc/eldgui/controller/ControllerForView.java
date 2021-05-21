package com.ericlam.mc.eldgui.controller;

import com.ericlam.mc.eldgui.model.Model;
import com.ericlam.mc.eldgui.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ControllerForView {

    Class<? extends View<? extends Model>> value();
}
