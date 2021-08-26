package com.ericlam.mc.eldgui.component.modifier;

import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * 可透過點擊更改組件屬性
 */
public interface Clickable extends Disable {

    /**
     * 點擊時的動作
     * @param event 點擊事件
     */
    void onClick(InventoryClickEvent event);

}
