package com.ericlam.mc.eldgui.component;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.component.factory.AttributeController;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.Optional;

public final class NumInputField extends AbstractComponent implements ClickableComponent {

    private final int min, max;

    private int value;

    public NumInputField(
            AttributeController attributeController,
            ItemStackService.ItemFactory itemFactory,
            int min,
            int max
    ) {
        super(attributeController, itemFactory);
        this.min = min;
        this.max = max;
        this.value = Optional.ofNullable(attributeController.getAttribute(Integer.class, getItem(), AttributeController.VALUE_TAG)).orElse(0);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if (event.isLeftClick()) {
            this.value = Math.min(max, value + 1);
        } else if (event.isRightClick()) {
            this.value = Math.max(min, value - 1);
        } else {
            return;
        }
        attributeController.setAttribute(getItem(), AttributeController.VALUE_TAG, this.value);
        itemFactory.lore(List.of("Input: " + this.value));
    }

}
