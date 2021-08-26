package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eldgui.component.ComponentFactory;
import org.bukkit.Material;

import java.util.List;

/**
 * 動態按鈕組件工廠
 */
public interface AnimatedButtonFactory extends ComponentFactory<AnimatedButtonFactory> {

    /**
     *
     * @param lore 組件敘述
     * @return this
     */
    AnimatedButtonFactory lores(List<List<String>> lore);

    /**
     *
     * @param seconds 動畫間隔(秒)
     * @return this
     */
    AnimatedButtonFactory interval(int seconds);

    /**
     *
     * @param materials 動態圖示
     * @return this
     */
    AnimatedButtonFactory icons(Material... materials);

    /**
     *
     * @param numbers 動態數量
     * @return this
     */
    AnimatedButtonFactory numbers(int... numbers);

    /**
     *
     * @param titles 動態標題
     * @return this
     */
    AnimatedButtonFactory titles(String... titles);

}
