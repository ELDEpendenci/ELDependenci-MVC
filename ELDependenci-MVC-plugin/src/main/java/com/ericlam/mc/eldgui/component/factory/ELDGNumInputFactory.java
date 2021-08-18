package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.component.AttributeController;
import com.ericlam.mc.eldgui.component.Component;
import com.ericlam.mc.eldgui.component.NumInputField;

import java.util.List;

public final class ELDGNumInputFactory extends AbstractComponentFactory<NumInputFactory> implements NumInputFactory{

    private int min;
    private int max;
    private boolean disabled;
    private String inputMessage;
    private String errorMessage;
    private long waitInput;

    public ELDGNumInputFactory(ItemStackService itemStackService, AttributeController attributeController) {
        super(itemStackService, attributeController);
    }

    @Override
    protected void defaultProperties() {
        this.min = 0;
        this.max = 64;
        this.disabled = false;
        this.inputMessage = "Input a number between ${min} to ${max}";
        this.errorMessage = "The number you input is not valid.";
        this.waitInput = 200L;
    }

    @Override
    public Component build(ItemStackService.ItemFactory itemFactory) {
        return new NumInputField(attributeController, itemFactory, min, max, disabled, inputMessage, errorMessage, waitInput);
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
        return this;
    }

    @Override
    public NumInputFactory waitForInput(long wait) {
        this.waitInput = wait;
        return this;
    }

    @Override
    public NumInputFactory messageInput(String message) {
        this.inputMessage = message;
        return this;
    }

    @Override
    public NumInputFactory messageInvalidNumber(String message) {
        this.errorMessage = message;
        return this;
    }

    @Override
    public NumInputFactory disabled() {
        this.disabled = true;
        return this;
    }
}
