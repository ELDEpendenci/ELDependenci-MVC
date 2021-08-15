package com.ericlam.mc.eldgui.component;

import org.bukkit.Material;

public interface ComponentFactory<Factory extends ComponentFactory<Factory>> {

    Factory icon(Material material);

    Factory bind(String key, Object value);

    Component create();

}
