package com.ericlam.mc.eldgui;

import org.bukkit.entity.Player;

public interface UIRenderer {

    void render(InventoryScope attributes, UIAction operation, Player player);

    default void onDestroy(InventoryScope scope, UIAction operation, Player player) {
    }

    default void onCreate(InventoryScope scope, Player player){
    }

}
