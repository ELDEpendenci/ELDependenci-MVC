package com.ericlam.mc.eldgui;

public interface InventoryService {

    UIDispatcher getUIDispatcher(String controller) throws UINotFoundException;
}
