package com.ericlam.mc.eldgui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public interface InventoryUI {

    void addClickEvent(char pattern, ClickCondition clickCondition, Consumer<InventoryClickEvent> consumer);

    void removeClickEvent(char pattern, ClickCondition clickCondition);

    void setItem(char pattern, ItemStack itemStack);

    Inventory getInventory();
}
