package com.ericlam.mc.eldgui.component;

import org.bukkit.Material;

public interface ComponentFactory<Factory extends ComponentFactory<Factory>> {

    Factory icon(Material material);

    /**
     *
     * @param amount 數字標記
     * @return this
     */
    Factory number(int amount);

    Factory bind(String key, Object value);

    Component create();

}
