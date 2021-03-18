package me.oska.gui.service;

import me.oska.gui.InventoryUI;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.function.BiConsumer;

/**
 * GUI Navigator Service
 */
public interface InventoryService {

    /**
     *
     */
    InventoryNavigator holder(HumanEntity player);

    /**
     *
     */
    void register(String route, Class<? extends InventoryUI<?>> renderer);

    /**
     *
     */
    Map<String, Class<? extends InventoryUI>> getRenderers();

    /**
     *
     */
    InventoryState getActiveInventory(String playerUUID);


    interface InventoryNavigator {

        /**
         *
         */
        void navigate(String route, Object state);

        /**
         *
         */
        void replace(String route, Object state);

        /**
         *
         */
        void reload(Object state);

        /**
         *
         */
        void goBack();

        /**
         *
         */
        void goBack(int num);
    }
}
