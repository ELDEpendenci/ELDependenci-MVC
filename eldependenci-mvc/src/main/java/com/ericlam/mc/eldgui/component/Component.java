package com.ericlam.mc.eldgui.component;

import org.bukkit.inventory.ItemStack;

/**
 * 組件(靜態)
 */
public interface Component {

    /**
     * 設置更新界面動作
     * @param updater 更新界面動作
     */
    void setUpdateHandler(Runnable updater);

    /**
     * 獲取組件内物品
     * @return 物品
     */
    ItemStack getItem();

}
