package com.ericlam.mc.eldgui.view;

/**
 * 標準界面，用於 controller 的方法中返回用
 * @param <T> 界面類型
 * @param <M> Model 類型
 */
public class BukkitView<T extends View<M>, M> {

    private final M model;
    private final Class<T> view;

    /**
     * 指定界面和 Model
     * @param view 界面類
     * @param model Model
     */
    public BukkitView(Class<T> view, M model) {
        this.model = model;
        this.view = view;
    }

    /**
     * 指定界面
     * @param view 界面類
     */
    public BukkitView(Class<T> view) {
        this(view, null);
    }

    public M getModel() {
        return model;
    }

    public Class<T> getView() {
        return view;
    }
}
