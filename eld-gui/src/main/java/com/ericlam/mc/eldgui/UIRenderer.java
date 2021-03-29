package com.ericlam.mc.eldgui;

import org.bukkit.entity.Player;

public interface UIRenderer {

    void render(InventoryScope attributes, UIOperation operation, Player player);

    default void onDestroy(InventoryScope scope, UIOperation operation, Player player) {
    }

    default void onCreate(InventoryScope scope, Player player){
    }

}
