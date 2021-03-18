package me.oska.gui.service;

import me.oska.gui.InventoryUI;
import me.oska.gui.InventoryWrapper;
import org.bukkit.entity.Player;

public interface InventoryService {

    <P, S> InventoryUI<P, S> buildInventory(P prop, S state);

    InventoryWrapper createWrapper(String key);

}
