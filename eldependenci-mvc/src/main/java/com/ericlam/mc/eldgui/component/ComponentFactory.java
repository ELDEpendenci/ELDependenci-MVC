package com.ericlam.mc.eldgui.component;

import org.bukkit.Material;

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
