package me.oska.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public interface InventoryUI<State> {
    Inventory createInventory(State state);
    void render(Inventory inventory, State state);
    void dispose();
    void click(InventoryClickEvent event, State state);
}
