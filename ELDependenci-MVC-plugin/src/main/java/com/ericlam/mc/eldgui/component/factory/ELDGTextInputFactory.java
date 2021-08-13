package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.component.Component;
import com.ericlam.mc.eldgui.component.TextInputField;

public final class ELDGTextInputFactory extends AbstractComponentFactory<TextInputFactory> implements TextInputFactory {

    public ELDGTextInputFactory(
            ItemStackService itemStackService,
            AttributeController attributeController
    ) {
        super(itemStackService, attributeController);
    }

    @Override
    public Component build(ItemStackService.ItemFactory itemFactory) {
        return new TextInputField(attributeController, itemFactory);
    }


    @Override
    public TextInputFactory label(String label) {
       return editItemByFactory(factory -> factory.display(label));
    }

    @Override
    public TextInputFactory bindInput(String field, String initValue) {
        bind(AttributeController.FIELD_TAG, field);
        return bind(AttributeController.VALUE_TAG, initValue);
    }
}
