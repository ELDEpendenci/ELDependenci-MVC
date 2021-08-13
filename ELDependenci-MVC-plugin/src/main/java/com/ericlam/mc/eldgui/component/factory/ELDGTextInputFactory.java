package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.component.Component;
import com.ericlam.mc.eldgui.component.TextInputField;

public final class ELDGTextInputFactory extends AbstractComponentFactory<TextInputFactory> implements TextInputFactory {

    private boolean disabled;

    public ELDGTextInputFactory(
            ItemStackService itemStackService,
            AttributeController attributeController
    ) {
        super(itemStackService, attributeController);
    }

    @Override
    protected void defaultProperties() {
        this.disabled = false;
    }

    @Override
    public Component build(ItemStackService.ItemFactory itemFactory) {
        return new TextInputField(attributeController, itemFactory, disabled);
    }


    @Override
    public TextInputFactory label(String label) {
        return editItemByFactory(factory -> factory.display(label));
    }

    @Override
    public TextInputFactory bindInput(String field, String initValue) {
        bind(AttributeController.FIELD_TAG, field);
        bind(AttributeController.VALUE_TAG, initValue);
        return editItemByFactory(f -> f.lore("Input: " + initValue));
    }

    @Override
    public TextInputFactory disabled() {
        this.disabled = true;
        return editItemByFactory(f -> f.lore("&cDisabled"));
    }
}
