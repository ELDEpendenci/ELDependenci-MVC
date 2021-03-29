package com.ericlam.mc.eldgui;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        this.clickType = List.of();
        this.actions = List.of();
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

    public List<InventoryAction> getActions() {
        return actions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClickCondition that = (ClickCondition) o;
        return Objects.equals(name, that.name) && Objects.equals(clickType, that.clickType) && Objects.equals(actions, that.actions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, clickType, actions);
    }
}
