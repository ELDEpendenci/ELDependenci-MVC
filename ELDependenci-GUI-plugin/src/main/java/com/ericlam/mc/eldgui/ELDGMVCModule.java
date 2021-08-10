package com.ericlam.mc.eldgui;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import org.bukkit.event.inventory.InventoryInteractEvent;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.function.BiPredicate;

public class ELDGMVCModule extends AbstractModule {

    private final ELDGMVCInstallation eldgmvcInstallation;

    public ELDGMVCModule(ELDGMVCInstallation eldgmvcInstallation) {
        this.eldgmvcInstallation = eldgmvcInstallation;
    }

    @Override
    protected void configure() {
        bind(new TypeLiteral<Map<String, Class<?>>>(){}).annotatedWith(Names.named("controllerMap")).toInstance(eldgmvcInstallation.getControllerMap());
        bind(new TypeLiteral<Map<Class<? extends Annotation>, BiPredicate<InventoryInteractEvent, Character>>>(){}).annotatedWith(Names.named("custom-qualifier")).toInstance(eldgmvcInstallation.getQualifierMap());
    }
}
