package com.ericlam.mc.eldgui.component;

import com.ericlam.mc.eld.services.ItemStackService;
import org.bukkit.Material;

import java.util.function.Consumer;

public interface ComponentFactory<Factory extends ComponentFactory<Factory>>{

    Factory icon(Material material);

    Factory bind(String key, Object value);

    Component create();

}
