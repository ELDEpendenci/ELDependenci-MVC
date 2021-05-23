package com.ericlam.mc.eldgui.controller;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface UIRequest {

    List<ItemStack> getItems(char pattern);

}
