package com.ericlam.mc.eldgui.component;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.component.factory.AttributeController;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractComponent implements Component {

    protected final AttributeController attributeController;
    protected final ItemStackService.ItemFactory itemFactory;

    public AbstractComponent(AttributeController attributeController, ItemStackService.ItemFactory itemFactory) {
        this.attributeController = attributeController;
        this.itemFactory = itemFactory;
    }

    @Override
    public ItemStack getItem() {
        return itemFactory.getItem();
    }
}
