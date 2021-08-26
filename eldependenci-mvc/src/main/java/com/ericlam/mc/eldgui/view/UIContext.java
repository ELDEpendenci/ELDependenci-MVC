package com.ericlam.mc.eldgui.view;

import com.ericlam.mc.eldgui.component.Component;
import com.ericlam.mc.eldgui.component.ComponentFactory;

/**
 * UI 操作類，主要用於界面渲染時存放組件和綁定屬性
 */
public interface UIContext {

    /**
     * 指定一個 pattern 進行操作
     * @param pattern pattern
     * @return this
     */
    PatternComponentBuilder pattern(char pattern);

    /**
     * 獲取已注冊的組件工廠
     * @param factoryCls 組件工廠類，必須為 interface
     * @param <T> 工廠類
     * @return 指定組件工廠
     */
    <T extends ComponentFactory<T>> T factory(Class<T> factoryCls);

    /**
     * 針對指定 pattern 的操作器
     */
    interface PatternComponentBuilder {

        /**
         * 填滿組件
         * @param component 組件
         * @return this
         */
        PatternComponentBuilder fill(Component component);

        /**
         * 新增組件
         * @param components 多個組件
         * @return this
         */
        PatternComponentBuilder components(Component... components);

        /**
         * 根據位置設定組件
         * @param pos 位置，僅爲 pattern 内的位置
         * @param component 組件
         * @return this
         */
        PatternComponentBuilder component(int pos, Component component);

        /**
         * 綁定該 pattern 内所有物品的屬性
         * @param key 鍵
         * @param value 數值
         * @return this
         */
        PatternComponentBuilder bindAll(String key, Object value);

        /**
         * 返回並執行第二個 pattern 的操作
         * @return UI context
         */
        UIContext and();

    }

}
