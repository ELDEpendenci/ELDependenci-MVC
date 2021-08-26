package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eldgui.component.ComponentFactory;

import java.time.LocalDate;

/**
 * 日期選擇器組件工廠
 */
public interface DateSelectorFactory extends ComponentFactory<DateSelectorFactory> {

    /**
     * 設置禁用
     * @return
     */
    DateSelectorFactory disabled();

    /**
     *
     * @param title 標題
     * @return this
     */
    DateSelectorFactory label(String title);

    /**
     * 綁定組件與 Model 屬性
     * @param field Model 屬性
     * @param initValue 初始化數值
     * @return this
     */
    DateSelectorFactory bindInput(String field, LocalDate initValue);

    /**
     *
     * @param message 要求玩家手動輸入的訊息
     * @return this
     */
    DateSelectorFactory inputMessage(String message);

    /**
     *
     * @param message 玩家輸入無效格式時發送的訊息
     * @return this
     */
    DateSelectorFactory invalidMessage(String message);

    /**
     *
     * @param maxWait 等待玩家輸入最大時間(ticks)
     * @return this
     */
    DateSelectorFactory waitForInput(long maxWait);

}
