package com.ericlam.mc.eldgui.component.modifier;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

/**
 * 可透過監聽指定玩家事件修改組件屬性
 * @param <E> 玩家事件
 */
public interface Listenable<E extends PlayerEvent> extends Activatable {

    /**
     * 開始監聽時的行爲
     * @param player 玩家
     */
    void onListen(Player player);

    /**
     *
     * @return 最長等待時間(ticks)
     */
    long getMaxWaitingTime();

    /**
     * 傳回事件後的動作
     * @param event 指定事件
     */
    void callBack(E event);

    /**
     *
     * @return 事件類型
     */
    Class<E> getEventClass();

}
