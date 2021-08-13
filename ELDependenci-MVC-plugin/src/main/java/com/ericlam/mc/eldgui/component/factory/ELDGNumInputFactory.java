package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.component.Component;
import com.ericlam.mc.eldgui.component.NumInputField;

public final class ELDGNumInputFactory extends AbstractComponentFactory<NumInputFactory> implements NumInputFactory{

    private int min = 0;
    private int max = 64;

    public ELDGNumInputFactory(ItemStackService itemStackService, AttributeController attributeController) {
        super(itemStackService, attributeController);
    }

    @Override
    public Component build(ItemStackService.ItemFactory itemFactory) {
        return new NumInputField(attributeController, itemFactory, min, max);
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
        return bind(AttributeController.VALUE_TAG, initValue);
    }
}
