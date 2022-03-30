package com.ericlam.mc.eldgui;

import org.jetbrains.annotations.Nullable;

/**
 * 玩家在打開界面到關閉界面時的數據容器，用於在各個 Controller 之間傳遞資料用
 */
public interface UISession {

    /**
     * 透過 key 獲取數據
     *
     * @param key 鍵
     * @param <T> 數據類型
     * @return 數據，可爲 null
     */
    @Nullable <T> T getAttribute(String key);

    /**
     * 透過 key 提取數據並在 Session 中刪除
     *
     * @param key 鍵
     * @param <T> 數據類型
     * @return 數據，可爲 null
     */
    @Nullable <T> T pollAttribute(String key);

    /**
     * 設置數據到 Session
     *
     * @param key   鍵
     * @param value 數值
     */
    void setAttribute(String key, Object value);


}
