package com.ericlam.mc.eldgui.event;

import org.bukkit.Material;
import org.bukkit.event.inventory.DragType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Deprecated
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DragHandler {

    BaseHandler base();

    DragType[] dragTypes() default {};

    Material[] dragItems() default {};

}
