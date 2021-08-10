package com.ericlam.mc.eldgui;

import com.ericlam.mc.eldgui.model.Model;
import org.bukkit.Bukkit;

import java.util.function.Consumer;

public class ELDGLiveData<E extends Model> implements MutableLiveData<E> {

    private final Consumer<E> updateView;
    private E data;

    public ELDGLiveData(E data, Consumer<E> updateView) {
        this.data = data;
        this.updateView = updateView;
    }

    @Override
    public void update(Consumer<E> consumer) {
        data.setChanged(true);
        consumer.accept(data);
        this.updateView.accept(data);
    }

    @Override
    public void postUpdate(Consumer<E> consumer) {
        Bukkit.getScheduler().runTask(ELDGPlugin.getPlugin(ELDGPlugin.class), () -> update(consumer));
    }

    @Override
    public void setValue(E data) {
        this.data = data;
        this.updateView.accept(data);
    }

    @Override
    public void postValue(E data) {
        Bukkit.getScheduler().runTask(ELDGPlugin.getPlugin(ELDGPlugin.class), () -> setValue(data));
    }

    @Override
    public E value() {
        return data;
    }
}
