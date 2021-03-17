package me.oska.gui.service;

import org.bukkit.entity.Player;
import me.oska.gui.InventoryNode;

/**
 * GUI Navigator Service
 */
public interface InventoryService {

    /**
     *
     * @param player
     * @return
     */
    InventoryNavigator holder(Player player);

    interface InventoryNavigator {

        /**
         *
         */
        void navigate(InventoryNode node);

        /**
         *
         */
        void replace(InventoryNode node);

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
