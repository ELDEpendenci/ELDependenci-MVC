package com.ericlam.mc.eldgui.component;

import com.ericlam.mc.eld.services.ItemStackService;
import org.bukkit.inventory.ItemStack;

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
