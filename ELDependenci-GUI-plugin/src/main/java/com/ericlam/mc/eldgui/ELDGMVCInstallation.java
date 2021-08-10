package com.ericlam.mc.eldgui;

import com.ericlam.mc.eldgui.controller.UIController;
import org.bukkit.event.inventory.InventoryInteractEvent;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;

public class ELDGMVCInstallation implements MVCInstallation{

    private final Map<String, Class<?>> controllerMap = new ConcurrentHashMap<>();
    private final Map<Class<? extends Annotation>, BiPredicate<InventoryInteractEvent, Character>> qualifierMap = new ConcurrentHashMap<>();

    private final ELDGPlugin plugin;

    public ELDGMVCInstallation(ELDGPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void registerQualifier(Class<? extends Annotation> qualifier, BiPredicate<InventoryInteractEvent, Character> predicate) {
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

    public Map<String, Class<?>> getControllerMap() {
        return controllerMap;
    }

    public Map<Class<? extends Annotation>, BiPredicate<InventoryInteractEvent, Character>> getQualifierMap() {
        return qualifierMap;
    }
}
