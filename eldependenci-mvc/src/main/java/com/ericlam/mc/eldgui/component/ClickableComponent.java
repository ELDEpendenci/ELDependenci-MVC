package com.ericlam.mc.eldgui.component;

import org.bukkit.event.inventory.InventoryClickEvent;

// change item attributes when clicked
public interface ClickableComponent extends Component, Disable {

    void onClick(InventoryClickEvent event);

}
