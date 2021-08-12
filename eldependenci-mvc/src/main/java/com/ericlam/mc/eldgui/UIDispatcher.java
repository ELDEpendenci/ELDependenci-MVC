package com.ericlam.mc.eldgui;

import com.google.common.annotations.Beta;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public interface UIDispatcher {

    void openFor(Player player);

    void openFor(Player player, Consumer<UISession> initSession);

    @Beta
    void openForGlobal(Player player);

}
