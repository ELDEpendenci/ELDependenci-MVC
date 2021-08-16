package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.component.AnimatedButton;
import com.ericlam.mc.eldgui.component.AttributeController;
import com.ericlam.mc.eldgui.component.Component;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public final class ELDGAnimatedButtonFactory extends AbstractComponentFactory<AnimatedButtonFactory> implements AnimatedButtonFactory {

    private String[][] lores;
    private Material[] icons;
    private String[] displays;
    private Integer[] numbers;
    private int seconds;

    public ELDGAnimatedButtonFactory(ItemStackService itemStackService, AttributeController attributeController) {
        super(itemStackService, attributeController);
    }

    @Override
    protected void defaultProperties() {
        this.lores = new String[0][0];
        this.icons = new Material[0];
        this.displays = new String[0];
        this.numbers = new Integer[0];
        this.seconds = 0;
    }

    @Override
    public Component build(ItemStackService.ItemFactory itemFactory) {
        return new AnimatedButton(attributeController, itemFactory, lores, icons, displays, numbers, seconds);
    }

    @Override
    public AnimatedButtonFactory lores(List<List<String>> lore) {
        this.lores = lore.stream().map(l -> l.toArray(String[]::new)).toArray(String[][]::new);
        return this;
    }

    @Override
    public AnimatedButtonFactory interval(int seconds) {
        this.seconds = seconds;
        return this;
    }

    @Override
    public AnimatedButtonFactory icons(Material... materials) {
        this.icons = materials;
        return this;
    }

    @Override
    public AnimatedButtonFactory numbers(int... numbers) {
        this.numbers = Arrays.stream(numbers).boxed().toArray(Integer[]::new);
        return this;
    }

    @Override
    public AnimatedButtonFactory titles(String... titles) {
        this.displays = titles;
        return this;
    }
}
