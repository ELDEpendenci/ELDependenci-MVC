package com.ericlam.mc.eldgui.component;

import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

/**
 * 物品屬性編輯器
 */
public interface AttributeController {

    /**
     * 預設綁定 Model 屬性數值 的 標識文字
     */
    String VALUE_TAG = "input-value";
    /**
     * 預設綁定 Model 屬性名稱 的 標識文字
     */
    String FIELD_TAG = "input-field";

    /**
     * 設置屬性
     * @param itemStack 物品
     * @param key 鍵
     * @param value 數值
     */
    void setAttribute(ItemStack itemStack, String key, Object value);

    /**
     * 設置 該 pattern 内所有物品的屬性
     * @param pattern pattern
     * @param key 鍵
     * @param value 數值
     */
    void setAttribute(char pattern, String key, Object value);

    /**
     * 獲取物品的屬性
     * @param item 物品
     * @param key 鍵
     * @param <C> 獲取類型
     * @return 數值
     */
    @Nullable
    <C> C getAttribute(ItemStack item, String key);

}
