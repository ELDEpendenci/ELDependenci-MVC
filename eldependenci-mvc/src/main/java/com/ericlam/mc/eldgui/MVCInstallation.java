package com.ericlam.mc.eldgui;

import com.ericlam.mc.eldgui.component.ComponentFactory;
import com.ericlam.mc.eldgui.exception.ExceptionViewHandler;
import com.ericlam.mc.eldgui.view.LoadingView;
import org.bukkit.event.inventory.InventoryInteractEvent;

import java.lang.annotation.Annotation;
import java.util.List;

public interface MVCInstallation {

    <A extends Annotation> void registerQualifier(Class<A> qualifier, QualifierFilter<A> predicate);

    void registerControllers(Class<?>... controllers);

    void addExceptionViewHandlers(List<Class<? extends ExceptionViewHandler>> controllers);

    void setGlobalExceptionHandler(Class<? extends ExceptionViewHandler> exceptionHandler);

    void setGlobalLoadingView(Class<? extends LoadingView> loadingView);

    <T extends ComponentFactory<T>, E extends T> void addComponentFactory(Class<T> factory, Class<E> implement);

    @FunctionalInterface
    interface QualifierFilter<A extends Annotation> {
        boolean checkIsPass(InventoryInteractEvent interactEvent, char pattern, A annotation);
    }

}
