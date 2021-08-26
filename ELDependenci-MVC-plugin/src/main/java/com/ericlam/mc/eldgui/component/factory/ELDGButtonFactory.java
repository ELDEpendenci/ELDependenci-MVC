package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.component.AttributeController;
import com.ericlam.mc.eldgui.component.Button;
import com.ericlam.mc.eldgui.component.Component;

public final class ELDGButtonFactory extends AbstractComponentFactory<ButtonFactory> implements ButtonFactory {

    public ELDGButtonFactory(ItemStackService itemStackService, AttributeController attributeController) {
        super(itemStackService, attributeController);
    }

    @Override
    protected void defaultProperties() {
    }

    @Override
    public Component build(ItemStackService.ItemFactory itemFactory) {
        return new Button(attributeController, itemFactory);
    }

    @Override
    public ButtonFactory title(String title) {
        return editItemByFactory(f -> f.display(title));
    }

    @Override
    public ButtonFactory lore(String... lore) {
        return editItemByFactory(f -> f.lore(lore));
    }

}
