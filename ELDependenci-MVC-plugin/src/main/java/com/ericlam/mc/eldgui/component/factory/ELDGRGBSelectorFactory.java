package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.component.AttributeController;
import com.ericlam.mc.eldgui.component.Component;
import com.ericlam.mc.eldgui.component.RGBSelector;
import org.bukkit.Color;

import java.util.List;

public final class ELDGRGBSelectorFactory extends AbstractComponentFactory<RGBSelectorFactory> implements RGBSelectorFactory {

    private boolean disabled;
    private String inputMessage;
    private String invalidMessage;
    private long maxWait;

    public ELDGRGBSelectorFactory(ItemStackService itemStackService, AttributeController attributeController) {
        super(itemStackService, attributeController);
    }

    @Override
    protected void defaultProperties() {
        this.disabled = false;
        this.inputMessage = "Input hex color (#??????) or rgb number (0~255 0~255 0~255)";
        this.invalidMessage = "Invalid format.";
        this.maxWait = 200L;
    }

    @Override
    public Component build(ItemStackService.ItemFactory itemFactory) {
        return new RGBSelector(attributeController, itemFactory, disabled, inputMessage, invalidMessage, maxWait);
    }

    @Override
    public RGBSelectorFactory label(String title) {
        return editItemByFactory(f -> f.display(title));
    }

    @Override
    public RGBSelectorFactory bindInput(String field, Color color) {
        bind(AttributeController.FIELD_TAG, field);
        bind(AttributeController.VALUE_TAG, color);
        return this;
    }

    @Override
    public RGBSelectorFactory inputMessage(String message) {
        this.inputMessage = message;
        return this;
    }

    @Override
    public RGBSelectorFactory invalidMessage(String message) {
        this.invalidMessage = message;
        return this;
    }

    @Override
    public RGBSelectorFactory waitForInput(long maxWait) {
        this.maxWait = maxWait;
        return this;
    }

    @Override
    public RGBSelectorFactory disabled() {
        this.disabled = true;
        return editItemByFactory(f -> f.lore(List.of("&cDisabled")));
    }
}
