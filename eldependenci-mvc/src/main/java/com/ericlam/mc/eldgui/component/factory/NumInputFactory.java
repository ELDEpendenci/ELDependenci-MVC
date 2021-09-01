package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eldgui.component.ComponentFactory;

/**
 * 數字輸入組件工廠，可用任意數字類型
 */
public interface NumInputFactory extends ComponentFactory<NumInputFactory> {

    /**
     *
     * @param label 標題
     * @return this
     */
    NumInputFactory label(String label);


    /**
     * 綁定指定數字類型
     * @param type 數字類型
     * @param <T> 數字類型
     * @return 泛型數字輸入組件工廠
     */
    <T extends Number> NumberTypeFactory<T> useNumberType(Class<T> type);

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
     * 設置禁用
     * @return this
     */
    NumInputFactory disabled();

    /**
     * 泛型數字輸入組件工廠
     * @param <T> 數字類型
     */
    interface NumberTypeFactory<T extends Number> {

        /**
         * 默認數值為 0
         * @param min 最少數字
         * @return this
         */
        NumberTypeFactory<T> min(T min);

        /**
         * 默認數值為 64
         * @param max 最大數字
         * @return this
         */
        NumberTypeFactory<T> max(T max);

        /**
         * 默認數值為 1
         * @param step 增加/減少數量
         * @return this
         */
        NumberTypeFactory<T> step(T step);


        /**
         * 綁定 model 屬性
         * @param field 屬性
         * @param initValue 初始數值
         * @return this
         */
        NumberTypeFactory<T> bindInput(String field, T initValue);

        /**
         * 返回組件工廠
         * @return this
         */
        NumInputFactory then();

    }

}
