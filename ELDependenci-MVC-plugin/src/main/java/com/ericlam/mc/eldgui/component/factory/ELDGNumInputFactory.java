package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.component.Component;
import com.ericlam.mc.eldgui.component.NumInputField;

public final class ELDGNumInputFactory extends AbstractComponentFactory<NumInputFactory> implements NumInputFactory{

    private int min;
    private int max;
    private boolean disabled;

    public ELDGNumInputFactory(ItemStackService itemStackService, AttributeController attributeController) {
        super(itemStackService, attributeController);
    }

    @Override
    protected void defaultProperties() {
        this.min = 0;
        this.max = 64;
        this.disabled = false;
    }

    @Override
    public Component build(ItemStackService.ItemFactory itemFactory) {
        return new NumInputField(attributeController, itemFactory, min, max, disabled);
    }


    @Override
    public NumInputFactory min(int min) {
        this.min = min;
        return this;
    }

    @Override
    public NumInputFactory max(int max) {
        this.max = max;
        return this;
    }

    @Override
    public NumInputFactory label(String label) {
        editItemByFactory(factory -> factory.display(label));
        return this;
    }

    @Override
    public NumInputFactory bindInput(String field, int initValue) {
        bind(AttributeController.FIELD_TAG, field);
        bind(AttributeController.VALUE_TAG, initValue);
        return editItemByFactory(f -> f.lore("Input: "+initValue));
    }

    @Override
    public NumInputFactory disabled() {
        this.disabled = true;
        return editItemByFactory(f -> f.lore("&cDisabled"));
    }
}
