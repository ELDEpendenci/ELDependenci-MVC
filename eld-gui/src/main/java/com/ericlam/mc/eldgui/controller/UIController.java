package com.ericlam.mc.eldgui.controller;

import org.bukkit.entity.Player;

public interface UIController {

    default void onDestroy(UIRequest request, Player player) {
    }

    default void onViewRendered(UIRequest request, Player player) {
    }

}
