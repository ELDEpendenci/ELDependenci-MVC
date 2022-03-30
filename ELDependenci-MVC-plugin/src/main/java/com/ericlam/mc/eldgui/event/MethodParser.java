package com.ericlam.mc.eldgui.event;

import org.bukkit.event.inventory.InventoryEvent;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@FunctionalInterface
public interface MethodParser {

    Object parseParameter(Annotation[] annotations, Type type, @Nullable InventoryEvent event);

}
