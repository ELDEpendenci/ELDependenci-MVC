package com.ericlam.mc.eldgui.component;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.component.factory.AttributeController;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public abstract class AbstractComponent implements Component {

    protected final AttributeController attributeController;
    protected final ItemStackService.ItemFactory itemFactory;

    private Runnable updateHandler = () -> {};

    public AbstractComponent(AttributeController attributeController, ItemStackService.ItemFactory itemFactory) {
        this.attributeController = attributeController;
        this.itemFactory = itemFactory;
    }

    @Override
    public void setUpdateHandler(Runnable updateHandler) {
        this.updateHandler = updateHandler;
    }

    protected void updateInventory(){
        this.updateHandler.run();
    }

    @Override
    public ItemStack getItem() {
        return itemFactory.getItem();
    }
}
