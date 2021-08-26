package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eldgui.component.ComponentFactory;
import org.bukkit.Material;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 選擇器組件工廠
 */
public interface SelectionFactory extends ComponentFactory<SelectionFactory> {

    /**
     *
     * @param title 標題
     * @return this
     */
    SelectionFactory label(String title);

    /**
     * 設置禁用
     * @return this
     */
    SelectionFactory disabled();

    /**
     *
     * @param selections 選擇列表
     * @param <T> 選擇元素的類型
     * @return this
     */
    <T> SelectionSettings<T> selectable(List<T> selections);

    /**
     * 更細節的選擇列表，包括細分圖示和數字標記等
     * @param type 選擇元素的類型
     * @param selectionBuilder 選擇元素建造器
     * @param <T> 選擇元素的類型
     * @return this
     */
    <T> SelectionSettings<T> selectable(Class<T> type, Consumer<SelectionBuilder<T>> selectionBuilder);


    /**
     * 選擇器設定
     * @param <T> 選擇元素類型
     */
    interface SelectionSettings<T> {

        /**
         * 定義每個選擇元素的文字顯示
         * @param toText 選擇元素的文字顯示
         * @return this
         */
        SelectionSettings<T> toDisplay(Function<T, String> toText);

        /**
         * 綁定組件與 Model 屬性
         * @param field Model 屬性
         * @param value 初始化數值
         * @return this
         */
        SelectionSettings<T> bindInput(String field, T value);

        /**
         * 返回工廠設置
         * @return 組件工廠
         */
        SelectionFactory then();

    }

    /**
     * 選擇元素建造器
     * @param <T> 選擇元素的類型
     */
    interface SelectionBuilder<T> {

        /**
         * 插入選擇元素
         * @param element 選擇元素
         * @return this
         */
        Selection<T> insert(T element);

        /**
         * 選擇元素設定
         * @param <T> 選擇元素的類型
         */
        interface Selection<T> {

            /**
             * 該元素的數字標記
             * @param amount 數字標記
             * @return this
             */
            Selection<T> number(int amount);

            /**
             * 該元素的圖示
             * @param icon 圖示
             * @return this
             */
            Selection<T> icon(Material icon);

            /**
             * 遞交。只有遞交後才能真正加入到組件中
             */
            void submit();

        }

    }


}
