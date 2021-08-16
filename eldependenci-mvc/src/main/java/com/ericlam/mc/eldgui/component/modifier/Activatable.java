package com.ericlam.mc.eldgui.component.modifier;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface Activatable {

    boolean shouldActivate(InventoryClickEvent e);

}
