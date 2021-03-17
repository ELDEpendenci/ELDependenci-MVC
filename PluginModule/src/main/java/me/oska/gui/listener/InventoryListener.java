package me.oska.gui.listener;

import me.oska.gui.InventoryNode;
import me.oska.gui.service.ELDInventoryService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Stack;

public class InventoryListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClick(InventoryClickEvent event) {
        String uuid = event.getWhoClicked().getUniqueId().toString();
        Stack<InventoryNode> inventoryList = ELDInventoryService.getByPlayer(uuid);
        if (inventoryList == null || inventoryList.size() == 0) {
            return;
        }

        InventoryNode inventory = inventoryList.peek();
        if (event.getClickedInventory() == inventory.getInventory()) {
            event.setCancelled(!inventory.options().clickableByDefault);
            inventory.click(event);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        String uuid = event.getPlayer().getUniqueId().toString();
        Stack<InventoryNode> inventoryList = ELDInventoryService.getByPlayer(uuid);
        if (inventoryList == null || inventoryList.size() == 0) {
            return;
        }

        if (inventoryList.get(0).isActive()) {
            return;
        }

        InventoryNode inventory = inventoryList.peek();
        player.openInventory(inventory.getInventory());
    }

}
