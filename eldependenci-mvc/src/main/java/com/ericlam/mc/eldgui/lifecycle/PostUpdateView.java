package com.ericlam.mc.eldgui.lifecycle;

import com.ericlam.mc.eldgui.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostUpdateView {

    Class<? extends View<?>> value();

}
