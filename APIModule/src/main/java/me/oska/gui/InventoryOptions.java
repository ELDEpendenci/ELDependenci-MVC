package me.oska.gui;

public class InventoryOptions {
    public static InventoryOptions DEFAULT = new InventoryOptions();

    public boolean clickableByDefault;

    public boolean refreshStateWhenGoBack;

    public boolean renderWhenGoBack;

    public boolean isGlobalSharedInventory;

    public InventoryOptions() {
        clickableByDefault = false;
        refreshStateWhenGoBack = false;
        renderWhenGoBack = false;
        isGlobalSharedInventory = false;
    }
}
