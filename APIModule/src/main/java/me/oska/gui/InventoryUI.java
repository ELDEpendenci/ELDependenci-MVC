package me.oska.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface InventoryUI<P, S> {

    void setOnRender(BiConsumer<P, S> biConsumer);

    void setOnDispose(Runnable dispose);

    void setOnInitialState(Supplier<S> supplier);

    void setOnUpdateState(Consumer<S> consumer);

    void setOnClick(Consumer<InventoryClickEvent> action);

    void setInventoryOptions(InventoryOptions options);

    void set(Set<Integer> index, ItemStack item);

    void set(Set<Integer> index, ItemStack item, Consumer<InventoryClickEvent> action);

    <P2, S2> void set(Set<Integer> index, ItemStack item, InventoryUI<P2, S2> gui);

    Inventory getInventory();

    boolean isActive();

    void updateState(Consumer<S> state);

    void disposeState();

    void openFor(Player player);

}
