package com.ericlam.mc.eldgui;

import org.bukkit.inventory.ItemStack;

public interface UIContext {

    boolean setItem(char pattern, int slot, ItemStack itemStack);

    boolean addItem(char pattern, ItemStack itemStack);

    void fillItem(char pattern, ItemStack itemStack);

    <C> void setAttribute(Class<C> type, ItemStack itemStack, String key, C value);


}
