package com.ericlam.mc.eldgui.component;

import org.bukkit.inventory.ItemStack;

// static component
public interface Component {

    <T> T getValue(String key, Class<T> type);

    ItemStack getItem();

}
