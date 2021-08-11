package com.ericlam.mc.eldgui;

import com.ericlam.mc.eldgui.exception.ExceptionViewHandler;
import org.bukkit.event.inventory.InventoryInteractEvent;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.BiPredicate;

public interface MVCInstallation {

    <A extends Annotation> void registerQualifier(Class<A> qualifier, QualifierFilter<A> predicate);

    void registerControllers(Class<?>... controllers);

    void addExceptionViewHandlers(List<Class<? extends ExceptionViewHandler>> controllers);

    void setGlobalExceptionHandler(Class<? extends ExceptionViewHandler> exceptionHandler);

    @FunctionalInterface
    interface QualifierFilter<A extends Annotation> {
        boolean checkIsPass(InventoryInteractEvent interactEvent, char pattern, A annotation);
    }

}
