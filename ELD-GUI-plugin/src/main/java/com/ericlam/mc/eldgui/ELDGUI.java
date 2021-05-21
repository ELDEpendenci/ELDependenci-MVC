package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.controller.FromPattern;
import com.ericlam.mc.eldgui.event.ELDGEventHandler;
import com.ericlam.mc.eldgui.event.MethodParseManager;
import com.ericlam.mc.eldgui.event.ReturnTypeManager;
import com.ericlam.mc.eldgui.event.click.ELDGClickEventHandler;
import com.ericlam.mc.eldgui.model.Model;
import com.ericlam.mc.eldgui.view.View;
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
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class ELDGUI<T extends Model> implements Listener {

    private final View<T> view;
    private final Inventory nativeInventory;
    private final ELDGContext eldgOperation = new ELDGContext();
    private final UISession session;
    private final Map<Character, List<Integer>> patternMasks = new LinkedHashMap<>();
    private final ItemStackService itemStackService;
    private final Player owner;
    private final Map<Class<? extends InventoryEvent>, ELDGEventHandler<? extends Annotation, ? extends InventoryEvent>> eventHandlerMap = new ConcurrentHashMap<>();
    private final ELDGLiveData<T> liveData;
    private final Class<? extends Model> modelClass;
    private final Consumer<Player> onDestroy;
    private final ViewJumper goTo;

    public ELDGUI(InventoryTemplate demoInventories,
                  View<T> view,
                  UIController controller,
                  ItemStackService itemStackService,
                  UISession session,
                  Player owner,
                  MethodParseFactory methodParseFactory,
                  Consumer<Player> onDestroy,
                  ViewJumper goTo
    ) {
        this.itemStackService = itemStackService;
        this.session = session;
        this.view = view;
        this.owner = owner;
        this.onDestroy = onDestroy;
        this.goTo = goTo;
        this.nativeInventory = Bukkit.createInventory(owner, demoInventories.rows * 9, ChatColor.translateAlternateColorCodes('&', demoInventories.name));
        this.eventHandlerMap.put(InventoryClickEvent.class,
                new ELDGClickEventHandler(controller,
                        methodParseFactory.buildParseManager(InventoryClickEvent.class, this::initializeParsers),
                        createReturnTypeManager()));
        this.renderFromTemplate(demoInventories);
        T model = view.renderAndCreateModel(session, eldgOperation, owner);
        this.modelClass = model.getClass();
        this.liveData = new ELDGLiveData<>(model, this::updateView);
        Bukkit.getServer().getPluginManager().registerEvents(this, ELDGPlugin.getPlugin(ELDGPlugin.class));
    }

    public void updateView(T model) {
        if (!model.isChanged()) return;
        view.onModelChanged(model, eldgOperation, owner);
        model.setChanged(false);
        liveData.setValue(model);
        owner.updateInventory();
    }

    private ReturnTypeManager createReturnTypeManager(){
        ReturnTypeManager returnTypeManager = new ReturnTypeManager();
        returnTypeManager.registerReturnType(type -> type.equals(void.class), (o) -> {});
        returnTypeManager.registerReturnType(type -> type.equals(String.class), (o) -> {
            if (!view.persist()) this.onDestroy.accept(owner);
            try {
                this.goTo.onJump(session, owner, (String) o);
            } catch (UINotFoundException e) {
                owner.sendMessage(e.getMessage());
                return;
            }
            this.destroy();
        });
        return returnTypeManager;
    }

    private void initializeParsers(MethodParseManager<? extends InventoryEvent> parser){
        parser.registerParser((t, annos) -> {
            if (t instanceof ParameterizedType){
                var parat = (ParameterizedType) t;
                return parat.getActualTypeArguments()[0] == modelClass && (parat.getRawType() == LiveData.class || parat.getRawType() == MutableLiveData.class);
            }
            return false;
        }, annotations -> liveData);
        parser.registerParser((t, annos) -> t.equals(UISession.class), annotations -> session);
        parser.registerParser((t, annos) -> {
            if (t instanceof ParameterizedType){
                var parat = (ParameterizedType) t;
                return parat.getActualTypeArguments()[0] == ItemStack.class && parat.getRawType() == List.class;
            }
            return false;
        }, annotations -> {
            FromPattern pattern = (FromPattern) Arrays.stream(annotations).filter(a -> a.annotationType() == FromPattern.class).findAny().orElseThrow(() -> new IllegalStateException("cannot find @FromPattern in List<ItemStack> parameters"));
            return eldgOperation.getItems(pattern.value());
        });
        parser.registerParser((t, annos) -> t.equals(Player.class), annotations -> owner);
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
                eventHandlerMap.values().forEach(handle -> handle.addCancel(pattern.charAt(0)));
            }
        }
    }

    public Inventory getNativeInventory() {
        return nativeInventory;
    }

    @SuppressWarnings("unchecked")
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        var handler = (ELDGEventHandler<? extends Annotation, InventoryClickEvent>) eventHandlerMap.get(e.getClass());
        if (handler == null) return;
        handler.onEventHandle(e,
                owner,
                nativeInventory,
                patternMasks
        );
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getPlayer() != this.owner) return;
        if (e.getInventory() != this.nativeInventory) return;
        if (view.persist()) return;
        destroy();
    }

    public void destroy() {
        eventHandlerMap.values().forEach(ELDGEventHandler::unloadAllHandlers);
        HandlerList.unregisterAll(this);
        patternMasks.clear();
        nativeInventory.clear();
        this.onDestroy.accept(owner);
    }

    private final class ELDGContext implements UIContext {

        @Override
        public boolean setItem(char pattern, int slot, ItemStack itemStack) {
            var slots = patternMasks.get(pattern);
            if (slots == null) return false;
            int order = 0;
            for (Integer s : slots) {
                if (order == slot) {
                    nativeInventory.setItem(s, itemStack);
                    owner.updateInventory();
                    return true;
                }
                order++;
            }
            return false;
        }


        public List<ItemStack> getItems(char pattern) {
            var slots = patternMasks.get(pattern);
            if (slots == null) return List.of();
            List<ItemStack> items = new ArrayList<>();
            for (int s : slots) {
                var item = Optional.ofNullable(nativeInventory.getItem(s)).orElseGet(() -> new ItemStack(Material.AIR));
                items.add(item);
                owner.updateInventory();
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
                owner.updateInventory();
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
                owner.updateInventory();
            }
        }

    }

}
