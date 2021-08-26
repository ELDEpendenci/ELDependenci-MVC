package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.component.ComponentFactory;

import java.util.function.Consumer;

/**
 * Bukkit 物品組件工廠
 */
public interface BukkitItemFactory extends ComponentFactory<BukkitItemFactory> {

    /**
     * 根據 {@link com.ericlam.mc.eld.services.ItemStackService.ItemFactory} 修改組件屬性
     * @param factoryConsumer 修改
     * @return this
     */
    BukkitItemFactory setupByItemFactory(Consumer<ItemStackService.ItemFactory> factoryConsumer);

}
