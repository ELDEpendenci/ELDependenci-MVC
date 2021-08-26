package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eldgui.component.ComponentFactory;
import org.bukkit.Material;

/**
 * 勾選框組件工廠
 */
public interface CheckboxFactory extends ComponentFactory<CheckboxFactory> {

    /**
     * 綁定組件與 Model 屬性
     * @param field Model 屬性
     * @param initValue 初始化數值
     * @return this
     */
    CheckboxFactory bindInput(String field, boolean initValue);

    /**
     *
     * @param material 勾選時的圖示
     * @return this
     */
    CheckboxFactory checked(Material material);

    /**
     *
     * @param material 取消勾選時圖示
     * @return this
     */
    CheckboxFactory unchecked(Material material);

    /**
     *
     * @param show 勾選時的標題
     * @return this
     */
    CheckboxFactory checkedDisplay(String show);

    /**
     *
     * @param show 取消勾選時的標題
     * @return this
     */
    CheckboxFactory uncheckedDisplay(String show);

    /**
     * 設置禁用
     * @return this
     */
    CheckboxFactory disabled();

}
