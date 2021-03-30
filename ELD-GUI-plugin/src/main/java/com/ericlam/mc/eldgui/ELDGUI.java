package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.services.ItemStackService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public final class ELDGUI implements Listener {

    private final UIRenderer renderer;
    private final Inventory nativeInventory;
    private final ELDGAction eldgOperation = new ELDGAction();
    private final InventoryScope attributes;
    private final Map<Character, List<Integer>> patternMasks = new LinkedHashMap<>();
    private final ItemStackService itemStackService;
    private final ELDGEventHandler eventHandler;
    private final Player owner;

    public ELDGUI(InventoryTemplate demoInventories,
                  UIRenderer renderer,
                  ItemStackService itemStackService,
                  InventoryScope attributes,
                  Player owner) {
        this.itemStackService = itemStackService;
        this.attributes = attributes;
        this.renderer = renderer;
        this.owner = owner;
        this.nativeInventory = Bukkit.createInventory(owner, demoInventories.rows * 9, ChatColor.translateAlternateColorCodes('&', demoInventories.name));
        renderer.onCreate(attributes, owner);
        this.renderFromTemplate(demoInventories);
        this.eventHandler = new ELDGEventHandler();
        this.eventHandler.loadAllHandlers(renderer);
        Bukkit.getServer().getPluginManager().registerEvents(this, ELDGPlugin.getPlugin(ELDGPlugin.class));
    }

    public void render() {
        renderer.render(attributes, eldgOperation, owner);
        owner.updateInventory();
    }

    private void renderFromTemplate(InventoryTemplate demoInventories) {
        this.patternMasks.clear();
        int line = 0;
        if (demoInventories.pattern.size() != demoInventories.rows)
            throw new IllegalStateException("界面模版的rows數量跟pattern行數不同。");
        for (String mask : demoInventories.pattern) {
            var masks = Arrays.copyOf(mask.toCharArray(), 9);
            for (int i = 0; i < masks.length; i++) {
                patternMasks.putIfAbsent(masks[i], new ArrayList<>());
                final int slots = i + 9 * line;
                patternMasks.get(masks[i]).add(slots);
            }
            line++;
        }
        for (String pattern : demoInventories.items.keySet()) {
            if (!this.patternMasks.containsKey(pattern.charAt(0))) continue;
            var slots = this.patternMasks.get(pattern.charAt(0));
            var itemDescriptor = demoInventories.items.get(pattern);
            var itemBuilder = itemStackService
                    .build(itemDescriptor.material)
                    .amount(itemDescriptor.amount);
            if (!itemDescriptor.name.isBlank()) itemBuilder.display(itemDescriptor.name);
            if (!itemDescriptor.lore.isEmpty()) itemBuilder.lore(itemDescriptor.lore);
            if (itemDescriptor.glowing) itemBuilder.enchant(Enchantment.DURABILITY, 1);
            var item = itemBuilder.getItem();
            for (Integer slot : slots) {
                this.nativeInventory.setItem(slot, item);
            }
            if (itemDescriptor.cancelMove) {
                eventHandler.addCancelClick(pattern.charAt(0));
            }
        }
    }

    public Inventory getNativeInventory() {
        return nativeInventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        eventHandler.onInventoryClick(e,
                owner,
                nativeInventory,
                patternMasks,
                eldgOperation,
                attributes
        );
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getPlayer() != this.owner) return;
        if (e.getInventory() != this.nativeInventory) return;
        destroy();
    }

    public void destroy() {
        eventHandler.unloadAllHandlers();
        HandlerList.unregisterAll(this);
        renderer.onDestroy(attributes, eldgOperation, owner);
        patternMasks.clear();
        nativeInventory.clear();
    }

    private final class ELDGAction implements UIAction {

        @Override
        public boolean setItem(char pattern, int slot, ItemStack itemStack) {
            var slots = patternMasks.get(pattern);
            if (slots == null) return false;
            int order = 0;
            for (Integer s : slots) {
                if (order == slot) {
                    nativeInventory.setItem(s, itemStack);
                    owner.updateInventory(); //
                    return true;
                }
                order++;
            }
            return false;
        }

        @Override
        public List<ItemStack> getItems(char pattern) {
            var slots = patternMasks.get(pattern);
            if (slots == null) return List.of();
            List<ItemStack> items = new ArrayList<>();
            for (int s : slots) {
                var item = Optional.ofNullable(nativeInventory.getItem(s)).orElseGet(() -> new ItemStack(Material.AIR));
                items.add(item);
                owner.updateInventory(); //
            }
            return List.copyOf(items);
        }

        @Override
        public boolean addItem(char pattern, ItemStack itemStack) {
            var slots = patternMasks.get(pattern);
            if (slots == null) return false;
            for (Integer s : slots) {
                var slotItem = nativeInventory.getItem(s);
                if (slotItem != null && slotItem.getType() != Material.AIR) continue;
                nativeInventory.setItem(s, itemStack);
                owner.updateInventory(); //
                return true;
            }
            return false;
        }

        @Override
        public void fillItem(char pattern, ItemStack itemStack) {
            var slots = patternMasks.get(pattern);
            if (slots == null) return;
            for (Integer s : slots) {
                nativeInventory.setItem(s, itemStack);
                owner.updateInventory(); //
            }
        }

        @Override
        public void rerender() {
            render();
        }

        @Override
        public void directTo(UIDispatcher dispatcher) {
            destroy();
            ((ELDGDispatcher) dispatcher).forward(owner, attributes.getSessionScope());
        }

        @Override
        public void setDirectItem(char pattern, UIDispatcher dispatcher) {
            eventHandler.addJumpTo(pattern, (ELDGDispatcher) dispatcher);
        }

    }

}
