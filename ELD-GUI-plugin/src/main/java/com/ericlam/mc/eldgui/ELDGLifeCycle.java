package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.ELDLifeCycle;
import com.ericlam.mc.eldgui.event.click.ClickEventParseManager;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;

public class ELDGLifeCycle implements ELDLifeCycle {

    @Inject
    private MethodParseFactory parseFactory;

    @Override
    public void onEnable(JavaPlugin javaPlugin) {
        parseFactory.registerParseManager(InventoryClickEvent.class, ClickEventParseManager.class);
    }

    @Override
    public void onDisable(JavaPlugin javaPlugin) {

    }
}
