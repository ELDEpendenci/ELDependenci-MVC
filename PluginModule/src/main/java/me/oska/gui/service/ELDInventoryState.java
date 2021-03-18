package me.oska.gui.service;

import me.oska.gui.InventoryUI;
import org.bukkit.inventory.Inventory;

public class ELDInventoryState implements InventoryState {
    private String route;
    private Object state;
    private Inventory inventory;
    private InventoryUI renderer;

    private ELDInventoryState(String route, Object state, Inventory inventory, InventoryUI renderer) {
        this.route = route;
        this.state = state;
        this.inventory =inventory;
        this.renderer = renderer;
    }

    protected static ELDInventoryState buildState(String route, Object state, Inventory inventory, InventoryUI renderer) {
        return new ELDInventoryState(route, state, inventory, renderer);
    }

    @Override
    public String getRoute() {
        return route;
    }

    @Override
    public Object getState() {
        return state;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public InventoryUI getRenderer() {
        return renderer;
    }
}