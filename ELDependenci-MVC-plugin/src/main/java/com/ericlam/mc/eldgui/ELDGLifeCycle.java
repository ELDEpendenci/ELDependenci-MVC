package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.ELDLifeCycle;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;

public class ELDGLifeCycle implements ELDLifeCycle {

    @Inject
    private InventoryService inventoryService;

    @Override
    public void onEnable(JavaPlugin javaPlugin) {

    }

    @Override
    public void onDisable(JavaPlugin javaPlugin) {
        ((ELDGInventoryService) inventoryService).onClose();
    }
}
