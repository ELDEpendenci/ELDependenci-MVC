package com.ericlam.mc.eldgui;

import com.ericlam.mc.eldgui.view.JumpToView;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface ViewJumper {
    void onJump(UISession session, Player player, JumpToView ui) throws UINotFoundException;
}
