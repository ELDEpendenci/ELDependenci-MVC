package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eldgui.component.ComponentFactory;

import java.time.LocalTime;

/**
 * 時間選擇器組件工廠 (24H)
 */
public interface TimeSelectorFactory extends ComponentFactory<TimeSelectorFactory> {

    /**
     * 設置禁用
     * @return this
     */
    TimeSelectorFactory disabled();

    /**
     *
     * @param title 標題
     * @return this
     */
    TimeSelectorFactory label(String title);

    /**
     * 綁定 Model 屬性與組件
     * @param field Model 屬性
     * @param time 初始化數值
     * @return this
     */
    TimeSelectorFactory bindInput(String field, LocalTime time);

    /**
     *
     * @param message 要求玩家輸入時的訊息
     * @return this
     */
    TimeSelectorFactory inputMessage(String message);

    /**
     *
     * @param message 玩家輸入無效時的訊息
     * @return this
     */
    TimeSelectorFactory invalidMessage(String message);

    /**
     *
     * @param maxWait 等待輸入時間 (ticks)
     * @return this
     */
    TimeSelectorFactory waitForInput(long maxWait);

}
