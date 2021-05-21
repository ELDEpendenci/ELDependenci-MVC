package com.ericlam.mc.eldgui;

import com.ericlam.mc.eldgui.model.Model;

import java.util.function.Consumer;

public interface LiveData<M extends Model> {

    void update(Consumer<M> consumer);

    void postUpdate(Consumer<M> consumer);

}
