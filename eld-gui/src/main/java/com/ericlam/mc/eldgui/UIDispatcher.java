package com.ericlam.mc.eldgui;

import com.google.common.annotations.Beta;
import org.bukkit.entity.Player;

public interface UIDispatcher {

    void openFor(Player player);

    @Beta
    void openForGlobal(Player player);

}
