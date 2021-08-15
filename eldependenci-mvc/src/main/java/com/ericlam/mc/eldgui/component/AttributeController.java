package com.ericlam.mc.eldgui.component;

import org.bukkit.inventory.ItemStack;

public interface AttributeController {

    String VALUE_TAG = "input-value";
    String FIELD_TAG = "input-field";

    void setAttribute(ItemStack itemStack, String key, Object value);

    void setAttribute(char pattern, String key, Object value);

    <C> C getAttribute(ItemStack item, String key);

}
