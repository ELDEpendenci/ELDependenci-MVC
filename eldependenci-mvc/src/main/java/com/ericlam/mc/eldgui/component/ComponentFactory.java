package com.ericlam.mc.eldgui.component;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * 基礎組件工廠，所有組件工廠必須繼承此類
 * @param <Factory> 組件工廠
 */
public interface ComponentFactory<Factory extends ComponentFactory<Factory>> {

    /**
     *
     * @param material 圖示
     * @return this
     */
    Factory icon(Material material);

    /**
     *
     * @param amount 數量
     * @return this
     */
    Factory number(int amount);

    /**
     * 以該物品為組件外觀原型。注意此舉將會覆蓋之前的所有設定 (包括綁定數值和外觀等)
     * @param item bukkit 物品
     * @return this
     */
    Factory mirror(ItemStack item);

    /**
     * 綁定組件屬性
     * @param key 鍵
     * @param value 數值
     * @return this
     */
    Factory bind(String key, Object value);

    /**
     * 創建組件
     * @return this
     */
    Component create();

}
