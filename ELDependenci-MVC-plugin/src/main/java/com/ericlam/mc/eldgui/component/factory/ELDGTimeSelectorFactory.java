package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.component.AttributeController;
import com.ericlam.mc.eldgui.component.Component;

import java.time.LocalTime;

public final class ELDGTimeSelectorFactory extends AbstractComponentFactory<TimeSelectorFactory> implements TimeSelectorFactory {

    private String input;
    private String invalid;
    private boolean disabled;
    private long maxWait;

    public ELDGTimeSelectorFactory(ItemStackService itemStackService, AttributeController attributeController) {
        super(itemStackService, attributeController);
    }

    @Override
    protected void defaultProperties() {
        this.input = "Input the value with format: <hour>:<minute>:<second>";
        this.invalid = "invalid format!";
        this.disabled = false;
        this.maxWait = 200L;
    }

    @Override
    public Component build(ItemStackService.ItemFactory itemFactory) {
        return null;
    }

    @Override
    public TimeSelectorFactory disabled() {
        this.disabled = true;
        return this;
    }

    @Override
    public TimeSelectorFactory label(String title) {
        return editItemByFactory(f -> f.display(title));
    }

    @Override
    public TimeSelectorFactory bindInput(String field, LocalTime time) {
        bind(AttributeController.FIELD_TAG, field);
        bind(AttributeController.FIELD_TAG, time);
        return this;
    }

    @Override
    public TimeSelectorFactory inputMessage(String message) {
        this.input = message;
        return this;
    }

    @Override
    public TimeSelectorFactory invalidMessage(String message) {
        this.invalid = message;
        return this;
    }

    @Override
    public TimeSelectorFactory waitForInput(long maxWait) {
        this.maxWait = maxWait;
        return this;
    }
}
