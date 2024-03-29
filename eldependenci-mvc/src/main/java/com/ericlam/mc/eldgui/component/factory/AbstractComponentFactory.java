package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.component.AttributeController;
import com.ericlam.mc.eldgui.component.Component;
import com.ericlam.mc.eldgui.component.ComponentFactory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * 用於繼承組件工廠，預設了很多事先的邏輯，方便創建
 * @param <Factory> 工廠
 */
public abstract class AbstractComponentFactory<Factory extends ComponentFactory<Factory>> implements ComponentFactory<Factory> {

    private final ItemStackService itemStackService;
    protected final AttributeController attributeController;
    private ItemStackService.ItemFactory itemFactory;

    private final Factory factory;

    @SuppressWarnings("unchecked")
    public AbstractComponentFactory(ItemStackService itemStackService, AttributeController attributeController) {
        this.itemStackService = itemStackService;
        this.attributeController = attributeController;
        this.itemFactory = itemStackService.build(Material.STONE);
        this.factory = (Factory) this;
        this.defaultProperties();
    }

    protected final Factory editItemByFactory(Consumer<ItemStackService.ItemFactory> factoryConsumer){
        factoryConsumer.accept(this.itemFactory);
        return factory;
    }

    @Override
    public Factory icon(Material material) {
        this.itemFactory.material(material);
        return factory;
    }

    @Override
    public Factory bind(String key, Object value) {
        attributeController.setAttribute(this.itemFactory.getItem(), key, value);
        return factory;
    }

    @Override
    public Factory number(int amount) {
        this.itemFactory.amount(amount);
        return factory;
    }

    @Override
    public Factory mirror(ItemStack item) {
        this.itemFactory = itemStackService.edit(item);
        return factory;
    }

    @Override
    public final Component create() {
        Component component = build(this.itemFactory);
        this.itemFactory = itemStackService.build(Material.STONE);
        this.defaultProperties();
        return component;
    }

    protected abstract void defaultProperties();

    public abstract Component build(ItemStackService.ItemFactory itemFactory);

}
