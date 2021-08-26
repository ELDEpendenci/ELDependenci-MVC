package com.ericlam.mc.eldgui;

import com.google.common.annotations.Beta;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

/**
 * 界面調度器
 */
public interface UIDispatcher {

    /**
     * 為玩家開啓
     * @param player 玩家
     */
    void openFor(Player player);

    /**
     * 為玩家開啓，並初始化 Session
     * @param player 玩家
     * @param initSession 初始化的 Session
     */
    void openFor(Player player, Consumer<UISession> initSession);

    /**
     * 全局打開 (目前暫不開放)
     * @param player 玩家
     */
    @Deprecated
    @Beta
    void openForGlobal(Player player);

}
