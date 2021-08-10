package com.ericlam.mc.eldgui.view;

public class BukkitView<T extends View<M>, M> {

    private final M model;
    private final Class<T> view;

    public BukkitView(Class<T> view, M model) {
        this.model = model;
        this.view = view;
    }

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
