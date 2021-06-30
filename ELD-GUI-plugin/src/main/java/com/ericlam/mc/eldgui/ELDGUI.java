package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.controller.FromPattern;
import com.ericlam.mc.eldgui.controller.ItemAttribute;
import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.controller.UIRequest;
import com.ericlam.mc.eldgui.event.ELDGEventHandler;
import com.ericlam.mc.eldgui.event.LifeCycleManager;
import com.ericlam.mc.eldgui.event.MethodParseManager;
import com.ericlam.mc.eldgui.event.ReturnTypeManager;
import com.ericlam.mc.eldgui.event.click.ELDGClickEventHandler;
import com.ericlam.mc.eldgui.event.drag.ELDGDragEventHandler;
import com.ericlam.mc.eldgui.lifecycle.OnDestroy;
import com.ericlam.mc.eldgui.lifecycle.OnRendered;
import com.ericlam.mc.eldgui.model.Model;
import com.ericlam.mc.eldgui.view.JumpToView;
import com.ericlam.mc.eldgui.view.View;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ELDGUI<T extends Model> implements Listener {

    private final View<T> view;
    private final Inventory nativeInventory;
    private final ELDGContext eldgContext = new ELDGContext();
    private final UISession session;
    private final Map<Character, List<Integer>> patternMasks = new LinkedHashMap<>();
    private final ItemStackService itemStackService;
    private final Player owner;
    private final Map<Class<? extends InventoryEvent>, ELDGEventHandler<? extends Annotation, ? extends InventoryEvent>> eventHandlerMap = new ConcurrentHashMap<>();
    private final Map<String, Function<InventoryEvent, ItemStack>> itemGetterMap = new ConcurrentHashMap<>();
    private final ELDGLiveData<T> liveData;
    private final Class<? extends Model> modelClass;
    private final Consumer<Player> onDestroy;
    private final ViewJumper goTo;
    private final LifeCycleManager lifeCycleManager;
    private transient boolean holdInventory = false;

    public ELDGUI(InventoryTemplate demoInventories,
                  View<T> view,
                  UIController controller,
                  ItemStackService itemStackService,
                  UISession session,
                  Player owner,
                  ManagerFactory managerFactory,
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
        MethodParseManager methodParseManager = managerFactory.buildParseManager(this::initMethodParseManager);
        this.lifeCycleManager = new LifeCycleManager(controller, methodParseManager);
        this.eventHandlerMap.put(InventoryClickEvent.class,
                new ELDGClickEventHandler(controller,
                        methodParseManager,
                        managerFactory.buildReturnTypeManager(this::initReturnTypeManager)));
        this.eventHandlerMap.put(InventoryDragEvent.class,
                new ELDGDragEventHandler(controller,
                        methodParseManager,
                        managerFactory.buildReturnTypeManager(this::initReturnTypeManager)));
        this.itemGetterMap.put(InventoryClickEvent.class.getSimpleName(), e -> ((InventoryClickEvent) e).getCurrentItem());
        this.itemGetterMap.put(InventoryDragEvent.class.getSimpleName(), e -> ((InventoryDragEvent) e).getOldCursor());
        this.renderFromTemplate(demoInventories);
        T model = view.renderAndCreateModel(session, eldgContext, owner);
        this.modelClass = model.getClass();
        this.liveData = new ELDGLiveData<>(model, this::updateView);
        lifeCycleManager.onLifeCycle(OnRendered.class);
        Bukkit.getServer().getPluginManager().registerEvents(this, ELDGPlugin.getPlugin(ELDGPlugin.class));
    }


    public void updateView(T model) {
        if (!model.isChanged()) return;
        view.onModelChanged(model, eldgContext, owner);
        model.setChanged(false);
        liveData.setValue(model);
        owner.updateInventory();
    }

    private void initReturnTypeManager(ReturnTypeManager returnTypeManager) {
        returnTypeManager.registerReturnType(type -> type.equals(void.class), (o) -> {
        });
        returnTypeManager.registerReturnType(type -> type.equals(String.class), (o) -> {
            if (!view.persist()) this.onDestroy.accept(owner);
            try {
                this.goTo.onJump(session, owner, new JumpToView((String) o));
            } catch (UINotFoundException e) {
                owner.sendMessage(e.getMessage());
                return;
            }
            if (!view.persist()) this.destroy();
        });
        returnTypeManager.registerReturnType(type -> type.equals(JumpToView.class), (o) -> {
            JumpToView toView = (JumpToView) o;
            if (!view.persist() && !toView.isKeepPreviousUI()) this.onDestroy.accept(owner);
            this.holdInventory = toView.isKeepPreviousUI();
            try {
                this.goTo.onJump(session, owner, toView);
            } catch (UINotFoundException e) {
                owner.sendMessage(e.getMessage());
                return;
            } finally {
                this.holdInventory = false;
            }
            if (!view.persist() && !toView.isKeepPreviousUI()) this.destroy();
        });
    }

    private void initMethodParseManager(MethodParseManager parser) {
        parser.registerParser((t, annos) -> {
            if (t instanceof ParameterizedType) {
                var parat = (ParameterizedType) t;
                return parat.getActualTypeArguments()[0] == modelClass && (parat.getRawType() == LiveData.class || parat.getRawType() == MutableLiveData.class);
            }
            return false;
        }, (annotations, t, e) -> liveData);
        parser.registerParser((t, annos) -> t.equals(UISession.class), (annotations, t, e) -> session);
        parser.registerParser((t, annos) -> {
            if (t instanceof ParameterizedType) {
                var parat = (ParameterizedType) t;
                return parat.getActualTypeArguments()[0] == ItemStack.class && parat.getRawType() == List.class;
            }
            return false;
        }, (annotations, t, e) -> {
            FromPattern pattern = (FromPattern) Arrays.stream(annotations).filter(a -> a.annotationType() == FromPattern.class).findAny().orElseThrow(() -> new IllegalStateException("cannot find @FromPattern in List<ItemStack> parameters"));
            if (pattern.fromDrag() && e instanceof InventoryDragEvent){
                Map<Integer, ItemStack> map = ((InventoryDragEvent) e).getNewItems();
                return map.entrySet().stream().filter(en -> Optional.ofNullable(patternMasks.get(pattern.value())).map(list -> list.contains(en.getKey())).orElse(false)).map(Map.Entry::getValue).collect(Collectors.toList());
            }
            return eldgContext.getItems(pattern.value());
        });
        parser.registerParser((t, annos) -> t.equals(UIRequest.class), (annotations, t, e) -> eldgContext);
        parser.registerParser((t, annos) -> t.equals(Player.class), (annotations, t, e) -> owner);
        parser.registerParser((t, annos) -> t.equals(ItemStack.class), (anno, t, e) -> getItemByEvent(e));
        parser.registerParser((t, annos) -> Arrays.stream(annos).anyMatch(a -> a.annotationType() == ItemAttribute.class) && t.equals(String.class), (annos, t, e) -> {
            var item = getItemByEvent(e);
            ItemAttribute attribute = (ItemAttribute) Arrays.stream(annos).filter(a -> a.annotationType() == ItemAttribute.class).findAny().orElseThrow(() -> new IllegalStateException("cannot find @ItemAttribute"));
            return eldgContext.getAttribute((Class<?>) t, item, attribute.value());
        });
        parser.registerParser((t, annos) -> t instanceof Class && InventoryInteractEvent.class.isAssignableFrom((Class<?>) t), (anno, t, e) -> e);
        parser.registerParser((t, annos) -> {
            if (t instanceof ParameterizedType){
                var parat = (ParameterizedType) t;
                return parat.getRawType() == Map.class && parat.getActualTypeArguments()[0] == Integer.class && parat.getActualTypeArguments()[1] == ItemStack.class;
            }
            return false;
        }, (annos, t, e) -> {
            if (e instanceof InventoryDragEvent){
                return ((InventoryDragEvent) e).getNewItems();
            }else{
                throw new IllegalStateException("not inventory drag event.");
            }
        });
    }

    private ItemStack getItemByEvent(InventoryEvent e) {
        return Optional.ofNullable(e).map(ee -> itemGetterMap.get(ee.getEventName())).map(f -> f.apply(e)).orElseThrow(() -> new IllegalStateException("no item return by the event or the event is null"));
    }

    public void resume() {
        view.onResume(session, eldgContext, owner);
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
                eventHandlerMap.values().forEach(handle -> handle.addCancel(pattern.charAt(0)));
            }
        }
    }

    public Inventory getNativeInventory() {
        return nativeInventory;
    }

    @SuppressWarnings("unchecked")
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        var handler = (ELDGEventHandler<? extends Annotation, InventoryClickEvent>) eventHandlerMap.get(e.getClass());
        if (handler == null) return;
        handler.onEventHandle(e,
                owner,
                nativeInventory,
                patternMasks
        );
    }

    @SuppressWarnings("unchecked")
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e){
        var handler = (ELDGEventHandler<? extends Annotation, InventoryDragEvent>) eventHandlerMap.get(e.getClass());
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
        if (view.persist() || this.holdInventory) return;
        destroy();
    }

    public void destroy() {
        eventHandlerMap.values().forEach(ELDGEventHandler::unloadAllHandlers);
        HandlerList.unregisterAll(this);
        lifeCycleManager.onLifeCycle(OnDestroy.class);
        patternMasks.clear();
        nativeInventory.clear();
        this.onDestroy.accept(owner);
    }

    private final class ELDGContext implements UIContext, UIRequest {

        @Override
        public boolean setItem(char pattern, int slot, ItemStack itemStack) {
            var slots = patternMasks.get(pattern);
            if (slots == null) return false;
            int order = 0;
            for (Integer s : slots) {
                if (order == slot) {
                    nativeInventory.setItem(s, itemStack);
                    return true;
                }
                order++;
            }
            return false;
        }

        public <C> C getAttribute(Class<C> type, ItemStack itemStack, String key) {
            var meta = itemStack.getItemMeta();
            if (meta == null)
                throw new IllegalStateException("cannot get attribute: " + key + ", this item has no item meta.");
            var container = meta.getPersistentDataContainer();
            try {
                var con = PersistentDataType.PrimitivePersistentDataType.class.getDeclaredConstructor(type);
                con.setAccessible(true);
                PersistentDataType<C, C> o = con.newInstance(type);
                return container.get(new NamespacedKey(ELDGPlugin.getPlugin(ELDGPlugin.class), key), o);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        public <C> void setAttribute(Class<C> type, ItemStack itemStack, String key, C value) {
            var meta = itemStack.getItemMeta();
            if (meta == null)
                throw new IllegalStateException("cannot get attribute: " + key + ", this item has no item meta.");
            var container = meta.getPersistentDataContainer();
            try {
                var con = PersistentDataType.PrimitivePersistentDataType.class.getDeclaredConstructor(type);
                con.setAccessible(true);
                PersistentDataType<C, C> o = con.newInstance(type);
                container.set(new NamespacedKey(ELDGPlugin.getPlugin(ELDGPlugin.class), key), o, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public List<ItemStack> getItems(char pattern) {
            var slots = patternMasks.get(pattern);
            if (slots == null) return List.of();
            List<ItemStack> items = new ArrayList<>();
            for (int s : slots) {
                var item = Optional.ofNullable(nativeInventory.getItem(s)).orElseGet(() -> new ItemStack(Material.AIR));
                items.add(item);
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
            }
        }

    }

}
