package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eldgui.component.ComponentFactory;

/**
 * 按鈕的組件工廠
 */
public interface ButtonFactory extends ComponentFactory<ButtonFactory> {

    /**
     *
     * @param title 標題
     * @return this
     */
    ButtonFactory title(String title);

    /**
     *
     * @param lore 敘述
     * @return this
     */
    ButtonFactory lore(String... lore);

    /**
     *
     * @param amount 數字標記
     * @return this
     */
    ButtonFactory number(int amount);

}
