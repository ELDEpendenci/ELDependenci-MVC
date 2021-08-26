package com.ericlam.mc.eldgui.component.modifier;

import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * 可設置是否被啓動
 */
public interface Activatable {

    /**
     *
     * @param e 點擊事件
     * @return 是否被啓動
     */
    boolean shouldActivate(InventoryClickEvent e);

}
