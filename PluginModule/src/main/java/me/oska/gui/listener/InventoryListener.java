package me.oska.gui.listener;

import me.oska.gui.InventoryUI;
import me.oska.gui.service.ELDInventoryService;
import me.oska.gui.service.InventoryService;
import me.oska.gui.service.InventoryState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import javax.inject.Inject;

public class InventoryListener implements Listener {

    @Inject
    private InventoryService service;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClick(InventoryClickEvent event) {
        String uuid = event.getWhoClicked().getUniqueId().toString();
        InventoryState renderer = service.getActiveInventory(uuid);
        if (renderer == null) {
            return;
        }

        if (event.getClickedInventory() != renderer.getInventory()) {
            return;
        }
        renderer.getRenderer().click(event, renderer.getState());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        String uuid = event.getPlayer().getUniqueId().toString();
        InventoryState renderer = service.getActiveInventory(uuid);
        if (renderer == null) {
            return;
        }

        if (event.getReason() == InventoryCloseEvent.Reason.OPEN_NEW) {
            return;
        }
        player.openInventory(renderer.getInventory());
    }

}
