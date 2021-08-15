package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eldgui.component.ComponentFactory;

/**
 * 數字輸入組件工廠
 */
public interface NumInputFactory extends ComponentFactory<NumInputFactory> {

    /**
     *
     * @param min 最少數字
     * @return this
     */
    NumInputFactory min(int min);

    /**
     *
     * @param max 最大數字
     * @return this
     */
    NumInputFactory max(int max);

    /**
     *
     * @param label 標題
     * @return this
     */
    NumInputFactory label(String label);

    /**
     * 綁定 model 屬性
     * @param field 屬性
     * @param initValue 初始數值
     * @return this
     */
    NumInputFactory bindInput(String field, int initValue);

    /**
     *
     * @param wait 等待輸入時間 (ticks)
     * @return this
     */
    NumInputFactory waitForInput(long wait);

    /**
     *
     * @param message 輸入時的訊息
     * @return this
     */
    NumInputFactory messageInput(String message);

    /**
     *
     * @param message 無效文字時的訊息
     * @return this
     */
    NumInputFactory messageInvalidNumber(String message);

    /**
     * 禁用組件
     * @return this
     */
    NumInputFactory disabled();

}
