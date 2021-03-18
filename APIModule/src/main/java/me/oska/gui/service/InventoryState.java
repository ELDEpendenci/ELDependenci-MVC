package me.oska.gui.service;

import me.oska.gui.InventoryUI;
import org.bukkit.inventory.Inventory;

public interface InventoryState {

    /**
     *
     */
    String getRoute();

    /**
     *
     */
    Object getState();

    /**
     *
     */
    Inventory getInventory();

    /**
     *
     */
    InventoryUI getRenderer();

}
