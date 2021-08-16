package com.ericlam.mc.eldgui.component.modifier;

import org.bukkit.event.inventory.InventoryClickEvent;

// change item attributes when clicked
public interface Clickable extends Disable {

    void onClick(InventoryClickEvent event);

}
