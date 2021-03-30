package com.ericlam.mc.eldgui;

import org.bukkit.inventory.ItemStack;

import java.rmi.server.UID;
import java.util.List;

public interface UIAction {

    boolean setItem(char pattern, int slot, ItemStack itemStack);

    List<ItemStack> getItems(char pattern);

    boolean addItem(char pattern, ItemStack itemStack);

    void fillItem(char pattern, ItemStack itemStack);

    void rerender();

    void directTo(UIDispatcher dispatcher);

    void setDirectItem(char pattern, UIDispatcher dispatcher);

}
