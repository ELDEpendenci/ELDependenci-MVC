package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.component.AttributeController;
import com.ericlam.mc.eldgui.component.Component;
import com.ericlam.mc.eldgui.component.TextInputField;

public final class ELDGTextInputFactory extends AbstractComponentFactory<TextInputFactory> implements TextInputFactory {

    private boolean disabled;
    private long maxWait;
    private String inputMessage;

    public ELDGTextInputFactory(
            ItemStackService itemStackService,
            AttributeController attributeController
    ) {
        super(itemStackService, attributeController);
    }

    @Override
    protected void defaultProperties() {
        this.disabled = false;
        this.maxWait = 200L;
        this.inputMessage = "Please Input a text within 10 seconds";
    }

    @Override
    public Component build(ItemStackService.ItemFactory itemFactory) {
        return new TextInputField(attributeController, itemFactory, disabled, maxWait, inputMessage);
    }


    @Override
    public TextInputFactory label(String label) {
        return editItemByFactory(factory -> factory.display(label));
    }

    @Override
    public TextInputFactory bindInput(String field, String initValue) {
        bind(AttributeController.FIELD_TAG, field);
        bind(AttributeController.VALUE_TAG, initValue);
        return this;
    }

    @Override
    public TextInputFactory waitForInput(long wait) {
        this.maxWait = wait;
        return this;
    }

    @Override
    public TextInputFactory messageInput(String message) {
        this.inputMessage = message;
        return this;
    }

    @Override
    public TextInputFactory disabled() {
        this.disabled = true;
        return this;
    }
}
