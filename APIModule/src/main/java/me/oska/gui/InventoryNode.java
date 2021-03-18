package me.oska.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@Deprecated
public abstract class InventoryNode<P, S> implements Cloneable {

    private final Map<Integer, Consumer<InventoryClickEvent>> actions;

    protected Inventory inventory;
    protected boolean isActive;
    protected S state;
    protected P props;

    public abstract InventoryOptions options();
    public abstract Inventory createInventory();
    public abstract S initialState();
    public abstract void render();
    protected abstract void dispose();

    public InventoryNode() {
        this(null, null);
    }

    public InventoryNode(P props) {
        this(props, null);
    }

    public InventoryNode(P props, S state) {
        this.isActive = true;
        this.inventory = this.createInventory();
        this.actions = new HashMap<>();
        this.props = props;
        this.state = state;
        if (state == null) {
            this.state = initialState();
        }
    }

    public void disposeState() {
        this.isActive = false;
        this.dispose();
    }

    public void setState(Function<S,S> update) {
        setState(update.apply(this.state));
    }

    public void setState(S state) {
        this.state = state;
        this.render();
    }

    protected void set(int index, ItemStack item) {
        this.set(index, item, null);
    }

    protected void set(int index, ItemStack item, Consumer<InventoryClickEvent> action) {
        this.inventory.setItem(index, item);
        if (action != null) {
            this.actions.put(index, action);
        }
    }

    protected void set(int[] index, ItemStack item) {
        this.set(index, item, null);
    }

    protected void set(int[] index, ItemStack item, Consumer<InventoryClickEvent> action) {
        for ( int i = 0; i < index.length; i++) {
            this.inventory.setItem(i, item);
            if (action != null) {
                this.actions.put(i, action);
            }
        }
    }

    public void click(InventoryClickEvent event) {
        Consumer<InventoryClickEvent> action = this.actions.getOrDefault(event.getSlot(), null);
        if (action != null) {
            action.accept(event);
        }
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public boolean isActive() {
        return this.isActive;
    }


    @Override
    public InventoryNode clone() {
        try {
            return (InventoryNode) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
