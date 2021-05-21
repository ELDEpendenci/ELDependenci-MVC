package com.ericlam.mc.eldgui;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface ViewJumper {
    void onJump(UISession session, Player player, String ui) throws UINotFoundException;
}
