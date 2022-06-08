package com.ericlam.mc.eldgui.middleware;

import com.ericlam.mc.eldgui.UISession;
import com.ericlam.mc.eldgui.view.BukkitView;
import org.bukkit.entity.Player;

/**
 * MiddleWare 專用的 context
 */
public interface InterceptContext {

    /**
     * 獲取玩家
     * @return 玩家
     */
    Player getPlayer();

    /**
     * 獲取 session 容器
     * @return session 容器
     */
    UISession getSession();

    /**
     * 設置要重導向的界面
     * @param view 界面
     */
    void setRedirect(BukkitView<?, ?> view);

}
