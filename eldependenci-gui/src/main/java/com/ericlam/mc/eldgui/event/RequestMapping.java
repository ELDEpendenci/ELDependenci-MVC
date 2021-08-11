package com.ericlam.mc.eldgui.event;

import com.ericlam.mc.eldgui.view.View;
import org.bukkit.event.inventory.InventoryInteractEvent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

    Class<? extends View<?>> view();

    Class<? extends InventoryInteractEvent> event();

    char pattern();

    boolean ignoreCancelled() default false;

}
