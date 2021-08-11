package com.ericlam.mc.eldgui;

import com.ericlam.mc.eldgui.exception.ExceptionViewHandler;
import com.ericlam.mc.eldgui.exception.HandleException;
import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.exception.HandleForControllers;
import org.bukkit.event.inventory.InventoryInteractEvent;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;

public class ELDGMVCInstallation implements MVCInstallation{

    private final Map<String, Class<?>> controllerMap = new ConcurrentHashMap<>();
    private final Map<Class<? extends Annotation>, QualifierFilter<? extends Annotation>> qualifierMap = new ConcurrentHashMap<>();
    private final Map<Class<?>, Class<? extends ExceptionViewHandler>> exceptionHandlerMap = new ConcurrentHashMap<>();
    private final Set<Class<? extends ExceptionViewHandler>> scopedExceptionHandlerSet = new HashSet<>();
    private Class<? extends ExceptionViewHandler> defaultExceptionHandler;
    private final ELDGPlugin plugin;

    public ELDGMVCInstallation(ELDGPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public <A extends Annotation> void registerQualifier(Class<A> qualifier, QualifierFilter<A> predicate) {
        this.qualifierMap.put(qualifier, predicate);
    }

    @Override
    public void registerControllers(Class<?>... controllers) {
        for (Class<?> controller : controllers) {
            if (!controller.isAnnotationPresent(UIController.class)) {
                plugin.getLogger().warning("controller "+controller+" do not have @UIController, skipped");
                continue;
            }
            UIController uic = controller.getAnnotation(UIController.class);
            this.controllerMap.put(uic.value(), controller);
        }
    }

    @Override
    public final void addExceptionViewHandlers(List<Class<? extends ExceptionViewHandler>> handlers) {
        for (Class<? extends ExceptionViewHandler> handler : handlers) {
            if (!handler.isAnnotationPresent(HandleForControllers.class)) {
                plugin.getLogger().info("exception view handler "+handler+" do not have @HandleForControllers, will use it as plugin scoped");
                this.scopedExceptionHandlerSet.add(handler);
                continue;
            }
            HandleForControllers uic = handler.getAnnotation(HandleForControllers.class);
            for (Class<?> controller : uic.value()) {
                this.exceptionHandlerMap.put(controller, handler);
            }
        }
    }

    @Override
    public void setGlobalExceptionHandler(Class<? extends ExceptionViewHandler> exceptionHandler) {
        this.defaultExceptionHandler = exceptionHandler;
    }


    public Map<String, Class<?>> getControllerMap() {
        return controllerMap;
    }

    public Map<Class<? extends Annotation>, QualifierFilter<? extends Annotation>> getQualifierMap() {
        return qualifierMap;
    }

    public Class<? extends ExceptionViewHandler> getDefaultExceptionHandler() {
        return defaultExceptionHandler;
    }

    public Set<Class<? extends ExceptionViewHandler>> getScopedExceptionHandlerSet() {
        return scopedExceptionHandlerSet;
    }

    public Map<Class<?>, Class<? extends ExceptionViewHandler>> getExceptionHandlerMap() {
        return exceptionHandlerMap;
    }
}
