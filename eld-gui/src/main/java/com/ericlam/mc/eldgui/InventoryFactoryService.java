package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.components.GroupConfiguration;

public interface InventoryFactoryService {

    InventoryUI generateFromTemplate(String template);

    InventoryUI generateFromTemplate(GroupConfiguration config);

    InventoryUI generateInventory(String[][] pattern);

    InventoryWrapper createWrapper();
}
