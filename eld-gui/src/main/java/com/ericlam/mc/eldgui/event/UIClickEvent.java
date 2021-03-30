package com.ericlam.mc.eldgui.event;

import com.ericlam.mc.eldgui.InventoryScope;
import com.ericlam.mc.eldgui.UIAction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class UIClickEvent extends UIEvent<InventoryClickEvent> {

    private final char patternClicked;
    private final ItemStack clickedItem;

    public UIClickEvent(Player owner, UIAction getAction, InventoryScope scope, InventoryClickEvent originEvent, char patternClicked, ItemStack clickedItem) {
        super(owner, getAction, scope, originEvent);
        this.patternClicked = patternClicked;
        this.clickedItem = clickedItem;
    }

    public char getPatternClicked() {
        return patternClicked;
    }

    public ItemStack getClickedItem() {
        return clickedItem;
    }
}
