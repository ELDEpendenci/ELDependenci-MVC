package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eldgui.component.ComponentFactory;

/**
 * 文字輸入組件工廠
 */
public interface TextInputFactory extends ComponentFactory<TextInputFactory> {

    /**
     *
     * @param label 標題
     * @return this
     */
    TextInputFactory label(String label);

    /**
     * 綁定 組件 與 Model 屬性
     * @param field Model 屬性
     * @param initValue 初始化數值
     * @return this
     */
    TextInputFactory bindInput(String field, String initValue);

    /**
     *
     * @param wait 等待玩家輸入時間(ticks)
     * @return this
     */
    TextInputFactory waitForInput(long wait);

    /**
     *
     * @param message 要求玩家輸入時的訊息
     * @return this
     */
    TextInputFactory messageInput(String message);

    /**
     * 設置禁用
     * @return this
     */
    TextInputFactory disabled();

}
