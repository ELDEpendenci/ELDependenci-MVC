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
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;

public final class ELDGUI implements Listener {

    private final UIRenderer renderer;
    private final Inventory nativeInventory;
    private final ELDGOperation eldgOperation = new ELDGOperation();
    private final InventoryScope attributes;
    private final Map<Character, List<Integer>> patternMasks = new LinkedHashMap<>();
    private final Map<Character, Map<ClickCondition, Consumer<InventoryClickEvent>>> clickMap = new LinkedHashMap<>();
    private final ItemStackService itemStackService;
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
        this.nativeInventory = Bukkit.createInventory(null, demoInventories.rows * 9, ChatColor.translateAlternateColorCodes('&', demoInventories.name));
        renderer.onCreate(attributes, owner);
        this.renderFromTemplate(demoInventories);
        Bukkit.getServer().getPluginManager().registerEvents(this, ELDGPlugin.getPlugin(ELDGPlugin.class));
    }

    public void render() {
        renderer.render(attributes, eldgOperation, owner);
        owner.updateInventory();
    }

    private void renderFromTemplate(InventoryTemplate demoInventories) {
        this.patternMasks.clear();
        int line = 0;
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
            if (itemDescriptor.cancelMove){
                eldgOperation.addClickEvent(pattern.charAt(0), ClickCondition.name("cancel-move"), e -> e.setCancelled(true));
            }
        }
    }

    public Inventory getNativeInventory() {
        return nativeInventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        var target = (Player)e.getWhoClicked();
        if (target != owner) return;
        if (this.nativeInventory != e.getClickedInventory()) return;
        if (e.getSlotType() != InventoryType.SlotType.CONTAINER) return;
        // owner.sendMessage(e.getAction().toString()); //
        char key = '\0';
        for (Character ch : patternMasks.keySet()) {
            if (patternMasks.get(ch).contains(e.getSlot())) {
                key = ch;
                break;
            }
        }
        if (key == '\0') return;
        if (!clickMap.containsKey(key)) return;
        var map = clickMap.get(key);
        map.forEach((c, ex) -> {
            if(!matchForHandler(c, e)) return;
            // owner.sendMessage("executing handler: "+c.getName()); //
            ex.accept(e);
        });

    }

    private boolean matchForHandler(ClickCondition c, InventoryClickEvent e){
        boolean equal;
        if (c.getActions().isEmpty() && c.getClickType().isEmpty()){
            equal = true;
        }else if (c.getActions().isEmpty()){
            equal = c.getClickType().contains(e.getClick());
        }else if (c.getClickType().isEmpty()){
            equal = c.getActions().contains(e.getAction());
        }else{
            equal = c.getActions().contains(e.getAction()) && c.getClickType().contains(e.getClick());
        }
        return equal;
    }

    private boolean matchForDelete(ClickCondition source, ClickCondition target){
        var name = source.getName() != null && target.getName() != null && source.getName().equals(target.getName());
        var actionEqual =  source.getActions().stream().anyMatch(c -> target.getActions().contains(c));
        var clickEqual = source.getClickType().stream().anyMatch(c -> target.getClickType().contains(c));
        return name || actionEqual || clickEqual;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        if (e.getInventory() != this.nativeInventory) return;
        destroy();
    }

    public void destroy() {
        HandlerList.unregisterAll(this);
        renderer.onDestroy(attributes, eldgOperation, owner);
        clickMap.clear();
        patternMasks.clear();
        nativeInventory.clear();
    }

    private final class ELDGOperation implements UIOperation {

        @Override
        public void addClickEvent(char pattern, ClickCondition clickCondition, Consumer<InventoryClickEvent> consumer) {
            clickMap.putIfAbsent(pattern, new LinkedHashMap<>());
            clickMap.get(pattern).put(clickCondition, consumer);
        }

        @Override
        public void removeClickEvent(char pattern, ClickCondition clickCondition) {
            if (!clickMap.containsKey(pattern)) return;
            var eventHandlers = clickMap.get(pattern);
            eventHandlers.keySet().stream().filter(k -> matchForDelete(k, clickCondition)).forEach(eventHandlers::remove);
        }

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
        public void redirect(UIDispatcher dispatcher) {
            destroy();
            ((ELDGDispatcher) dispatcher).forward(owner, attributes.getSessionScope());
        }
    }

}
