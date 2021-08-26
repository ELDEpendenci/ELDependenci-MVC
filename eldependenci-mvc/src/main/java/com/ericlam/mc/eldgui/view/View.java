package com.ericlam.mc.eldgui.view;

/**
 * 界面，根據 Model 進行渲染
 * @param <T> Model 類
 */
public interface View<T> {

    /**
     * 渲染界面
     * @param model Model
     * @param context UI渲染器
     */
    void renderView(T model, UIContext context);


}
