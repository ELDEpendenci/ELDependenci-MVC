package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eldgui.component.ComponentFactory;
import org.bukkit.Material;

import java.util.List;

public interface AnimatedButtonFactory extends ComponentFactory<AnimatedButtonFactory> {

    AnimatedButtonFactory lores(List<List<String>> lore);

    AnimatedButtonFactory interval(int seconds);

    AnimatedButtonFactory icons(Material... materials);

    AnimatedButtonFactory numbers(int... numbers);

    AnimatedButtonFactory titles(String... titles);

}
