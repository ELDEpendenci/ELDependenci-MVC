package me.oska.gui;

public interface InventoryWrapper {

    <P,S> void addInventory(String key, InventoryUI<P, S> ui);

}
