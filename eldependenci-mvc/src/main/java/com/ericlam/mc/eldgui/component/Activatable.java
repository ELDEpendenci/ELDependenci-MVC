package com.ericlam.mc.eldgui.component;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface Activatable {

    boolean shouldActivate(InventoryClickEvent e);

}
