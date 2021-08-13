package com.ericlam.mc.eldgui.view;

import org.bukkit.inventory.ItemStack;

public interface UIContextLegacy {

    boolean setItem(char pattern, int slot, ItemStack itemStack);

    boolean addItem(char pattern, ItemStack itemStack);

    void fillItem(char pattern, ItemStack itemStack);


}
