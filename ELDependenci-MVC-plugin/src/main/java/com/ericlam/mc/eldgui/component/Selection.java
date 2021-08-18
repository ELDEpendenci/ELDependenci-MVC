package com.ericlam.mc.eldgui.component;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.CircuitIterator;
import com.ericlam.mc.eldgui.ELDGPlugin;
import com.ericlam.mc.eldgui.component.factory.ELDGSelectionFactory;
import com.ericlam.mc.eldgui.component.modifier.Clickable;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class Selection<T> extends AbstractComponent implements Clickable {

    private final boolean disabled;
    private final ELDGSelectionFactory.ELDGSelectionSettings<T> selectionSettings;
    private final CircuitIterator<T> elementIterator;

    @SuppressWarnings("unchecked")
    public Selection(
            AttributeController attributeController,
            ItemStackService.ItemFactory itemFactory,
            boolean disabled,
            ELDGSelectionFactory.ELDGSelectionSettings<T> selectionSettings
    ) {
        super(attributeController, itemFactory);
        this.disabled = disabled;
        this.selectionSettings = selectionSettings;
        T currentValue = Optional.ofNullable((T) attributeController.getAttribute(getItem(), AttributeController.VALUE_TAG)).orElseGet(() -> selectionSettings.getElements().get(0));
        this.elementIterator = new CircuitIterator<>(selectionSettings.getElements(), selectionSettings.getElements().indexOf(currentValue));
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        T currentValue;
        if (event.isLeftClick()) {
            currentValue = elementIterator.previous();
        } else if (event.isRightClick()) {
            currentValue = elementIterator.next();
        } else {
            return;
        }
        attributeController.setAttribute(getItem(), AttributeController.VALUE_TAG, currentValue);
        itemFactory.lore(
                selectionSettings.getElements()
                        .stream()
                        .map(e -> {
                            if (currentValue == e) {
                                return "&f&l- " + selectionSettings.getToText().apply(e);
                            } else {
                                return "&7- " + selectionSettings.getToText().apply(e);
                            }
                        })
                        .collect(Collectors.toList())
        );
        if (currentValue != null) {
            var amounts = selectionSettings.getAmounts();
            var icons = selectionSettings.getIcons();
            if (amounts.containsKey(currentValue)) {
                itemFactory.amount(amounts.get(currentValue));
            }
            if (icons.containsKey(currentValue)) {
                itemFactory.material(icons.get(currentValue));
            }
        }
        this.updateInventory();
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }
}
