package com.ericlam.mc.eldgui.controller;

import com.ericlam.mc.eldgui.UISession;
import com.ericlam.mc.eldgui.view.View;
import org.bukkit.entity.Player;

/**
 * 界面生命周期挂鈎
 */
public interface ViewLifeCycleHook {

    /**
     * 新界面創建後
     * @param player 玩家
     * @param view 創建後的新界面
     * @param session Session
     */
    void postUpdateView(Player player, Class<View<?>> view, UISession session);

    /**
     * 界面摧毀前(更新前)
     * @param player 玩家
     * @param view 舊界面
     * @param session Session
     */
    void preViewDestroy(Player player, Class<View<?>> view, UISession session);

}
