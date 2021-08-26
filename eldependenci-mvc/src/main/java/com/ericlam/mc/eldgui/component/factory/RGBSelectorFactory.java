package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eldgui.component.ComponentFactory;
import org.bukkit.Color;

/**
 * RGB 顔色選擇器組件工廠
 */
public interface RGBSelectorFactory extends ComponentFactory<RGBSelectorFactory> {

    /**
     *
     * @param title 標題
     * @return this
     */
    RGBSelectorFactory label(String title);

    /**
     * 綁定組件與 Model 屬性
     * @param field Model 屬性
     * @param color 初始化數值
     * @return this
     */
    RGBSelectorFactory bindInput(String field, Color color);

    /**
     *
     * @param message 請求輸入時的訊息
     * @return this
     */
    RGBSelectorFactory inputMessage(String message);

    /**
     *
     * @param message 玩家輸入無效格式時的訊息
     * @return this
     */
    RGBSelectorFactory invalidMessage(String message);

    /**
     *
     * @param maxWait 最長等待輸入時間 (ticks)
     * @return this
     */
    RGBSelectorFactory waitForInput(long maxWait);

    /**
     * 設置禁用
     * @return this
     */
    RGBSelectorFactory disabled();

}
