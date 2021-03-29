package com.ericlam.mc.eldgui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface UIOperation {

    void addClickEvent(char pattern, ClickCondition clickCondition, Consumer<InventoryClickEvent> consumer);

    void removeClickEvent(char pattern, ClickCondition clickCondition);

    boolean setItem(char pattern, int slot, ItemStack itemStack);

    List<ItemStack> getItems(char pattern);

    boolean addItem(char pattern, ItemStack itemStack);

    void fillItem(char pattern, ItemStack itemStack);

    void rerender();

    void redirect(UIDispatcher dispatcher);
}
