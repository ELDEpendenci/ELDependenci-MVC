package com.ericlam.mc.eldgui.component;

import com.ericlam.mc.eld.services.ItemStackService;
import org.bukkit.Material;

public interface ComponentFactory<Factory extends ComponentFactory<Factory>>{

    Factory icon(Material material);

    Factory title(String title);

    Factory lore(String... lore);

    Factory bind(String key, Object value);

    Factory editByItemFactory(ItemStackService.ItemFactory itemFactory);

    Component create();

}
