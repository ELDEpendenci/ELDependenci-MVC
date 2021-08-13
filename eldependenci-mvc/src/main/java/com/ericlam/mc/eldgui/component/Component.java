package com.ericlam.mc.eldgui.component;

import org.bukkit.inventory.ItemStack;

// static component
public interface Component {

    void setUpdateHandler(Runnable updater);

    ItemStack getItem();

}
