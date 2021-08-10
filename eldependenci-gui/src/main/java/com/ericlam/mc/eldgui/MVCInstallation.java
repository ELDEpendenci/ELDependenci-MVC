package com.ericlam.mc.eldgui;

import org.bukkit.event.inventory.InventoryInteractEvent;

import java.lang.annotation.Annotation;
import java.util.function.BiPredicate;

public interface MVCInstallation {

    void registerQualifier(Class<? extends Annotation> qualifier, BiPredicate<InventoryInteractEvent, Character> predicate);

    void registerControllers(Class<?>... controllers);

}
