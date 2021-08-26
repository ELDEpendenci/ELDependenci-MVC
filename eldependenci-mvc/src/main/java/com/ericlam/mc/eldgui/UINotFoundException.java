package com.ericlam.mc.eldgui;

/**
 * 找不到 Controller 時抛出的異常。詳見 {@link InventoryService#getUIDispatcher(String)}
 */
public class UINotFoundException extends Exception {
    public UINotFoundException(String message) {
        super(message);
    }
}
