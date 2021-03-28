package com.ericlam.mc.eldgui;

import org.bukkit.inventory.Inventory;

public interface InventoryWrapper {

    void addInventory(String name, InventoryUI ui);

    void addPageInventory(int insert, InventoryUI ui);

    void addPageInventory(InventoryUI ui);

    Inventory getFirstInventory();
}
