package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.BukkitLifeCycle;
import com.ericlam.mc.eld.ELDLifeCycle;
import com.ericlam.mc.eld.services.ScheduleService;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;

public class ELDGLifeCycle implements BukkitLifeCycle {

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
