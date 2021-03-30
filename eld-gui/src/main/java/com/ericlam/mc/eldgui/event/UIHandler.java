package com.ericlam.mc.eldgui.event;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UIHandler {

    char[] patterns() default {};

    InventoryAction[] filterActions() default {};

    ClickType[] filterClicks() default {};

    int order() default 1;

    boolean ignoreCancelled() default false;

}
