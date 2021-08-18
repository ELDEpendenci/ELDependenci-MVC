package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.component.ComponentFactory;

import java.util.function.Consumer;

public interface BukkitItemFactory extends ComponentFactory<BukkitItemFactory> {

    BukkitItemFactory setupByItemFactory(Consumer<ItemStackService.ItemFactory> factoryConsumer);

}
