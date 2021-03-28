package com.ericlam.mc.eldgui;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;

import java.util.List;

public class ClickCondition {

    public static ClickCondition name(String name){
        return new ClickCondition(name);
    }

    public static ClickCondition clickType(List<ClickType> type){
        return new ClickCondition(null).setClickType(type);
    }

    public static ClickCondition actions(List<InventoryAction> actions){
        return new ClickCondition(null).setActions(actions);
    }

    private final String name;

    private List<ClickType> clickType;

    private List<InventoryAction> actions;

    public ClickCondition(String name) {
        this.name = name;
    }

    public List<InventoryAction> getActions() {
        return actions;
    }

    public ClickCondition setActions(List<InventoryAction> actions) {
        this.actions = actions;
        return this;
    }

    public ClickCondition setClickType(List<ClickType> clickType) {
        this.clickType = clickType;
        return this;
    }

    public String getName() {
        return name;
    }

    public List<ClickType> getClickType() {
        return clickType;
    }
}
