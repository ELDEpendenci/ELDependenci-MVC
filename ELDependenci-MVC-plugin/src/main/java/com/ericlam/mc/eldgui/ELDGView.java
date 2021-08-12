package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.services.ConfigPoolService;
import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.component.Component;
import com.ericlam.mc.eldgui.component.ComponentFactory;
import com.ericlam.mc.eldgui.view.*;
import com.google.inject.Injector;
import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class ELDGView<T> {

    private static final Map<Class<?>, PersistentDataType<?, ?>> PERSISTENT_DATA_TYPE_MAP = new ConcurrentHashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(ELDGView.class);

    static {
        PERSISTENT_DATA_TYPE_MAP.put(Byte.class, PersistentDataType.BYTE);
        PERSISTENT_DATA_TYPE_MAP.put(byte.class, PersistentDataType.BYTE);
        PERSISTENT_DATA_TYPE_MAP.put(Short.class, PersistentDataType.SHORT);
        PERSISTENT_DATA_TYPE_MAP.put(short.class, PersistentDataType.SHORT);
        PERSISTENT_DATA_TYPE_MAP.put(Integer.class, PersistentDataType.INTEGER);
        PERSISTENT_DATA_TYPE_MAP.put(int.class, PersistentDataType.INTEGER);
        PERSISTENT_DATA_TYPE_MAP.put(Long.class, PersistentDataType.LONG);
        PERSISTENT_DATA_TYPE_MAP.put(long.class, PersistentDataType.LONG);
        PERSISTENT_DATA_TYPE_MAP.put(Float.class, PersistentDataType.FLOAT);
        PERSISTENT_DATA_TYPE_MAP.put(float.class, PersistentDataType.FLOAT);
        PERSISTENT_DATA_TYPE_MAP.put(Double.class, PersistentDataType.DOUBLE);
        PERSISTENT_DATA_TYPE_MAP.put(double.class, PersistentDataType.DOUBLE);
        PERSISTENT_DATA_TYPE_MAP.put(String.class, PersistentDataType.STRING);
        PERSISTENT_DATA_TYPE_MAP.put(byte[].class, PersistentDataType.BYTE_ARRAY);
        PERSISTENT_DATA_TYPE_MAP.put(int[].class, PersistentDataType.INTEGER_ARRAY);
        PERSISTENT_DATA_TYPE_MAP.put(long[].class, PersistentDataType.LONG_ARRAY);
    }


    private final Inventory nativeInventory;
    private final Map<Character, List<Integer>> patternMasks = new LinkedHashMap<>();
    private final Map<Character, List<Component>> componentMap = new HashMap<>();
    private final Map<Class<? extends ComponentFactory<?>>, ComponentFactory<?>> factoryMap = new HashMap<>();
    private final Set<Character> cancelMovePatterns = new HashSet<>();
    private final View<T> view;
    private final ELDGContext eldgContext = new ELDGContext();


    public ELDGView(
            BukkitView<View<T>, T> bukkitView,
            ConfigPoolService configPoolService,
            ItemStackService itemStackService,
            Injector injector,
            List<Class<? extends ComponentFactory<?>>> componentFactory
    ) {
        for (Class<? extends ComponentFactory<?>> f : componentFactory) {
            this.factoryMap.put(f, injector.getInstance(f));
        }
        Class<View<T>> viewCls = bukkitView.getView();
        InventoryTemplate template;
        if (viewCls.isAnnotationPresent(UseTemplate.class)) {
            UseTemplate useTemplate = viewCls.getAnnotation(UseTemplate.class);
            var pool = configPoolService.getPool(useTemplate.groupResource());
            if (pool == null)
                throw new IllegalStateException("config pool is not loaded: " + useTemplate.groupResource());
            template = Optional.ofNullable(pool.get(useTemplate.template())).orElseThrow(() -> new IllegalStateException("Cannot find template: " + useTemplate.template()));
        } else if (viewCls.isAnnotationPresent(ViewDescriptor.class)) {
            ViewDescriptor viewDescriptor = viewCls.getAnnotation(ViewDescriptor.class);
            template = new CodeInventoryTemplate(viewDescriptor);
        } else {
            throw new IllegalStateException("view is lack of either @UseTemplate or @ViewDescriptor annotation.");
        }
        InventoryTemplate inventoryTemplate = template;
        Map<String, Object> objectFieldMap = ELDGUI.reflectToMap(bukkitView.getModel());
        String inventoryTitle = StrSubstitutor.replace(template.name, objectFieldMap);
        this.nativeInventory = Bukkit.createInventory(null, template.rows * 9, ChatColor.translateAlternateColorCodes('&', inventoryTitle));
        this.view = this.initializeView(viewCls);
        // === test only ===
        this.view.setItemStackService(itemStackService);
        // ========
        this.renderFromTemplate(inventoryTemplate, itemStackService);
        // either
        this.view.renderView(bukkitView.getModel(), eldgContext);
        this.view.renderView(bukkitView.getModel(), (ViewContext) null);
        //
        // test
        var p = ELDGPlugin.getPlugin(ELDGPlugin.class);
        Bukkit.getServer().getPluginManager().registerEvent(AsyncPlayerChatEvent.class, new Listener() {
        }, EventPriority.NORMAL, new EventExecutor() {
            @Override
            public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {

            }
        }, p);
    }


    public Inventory getNativeInventory() {
        return nativeInventory;
    }

    public View<?> getView() {
        return view;
    }

    public Set<Character> getCancelMovePatterns() {
        return cancelMovePatterns;
    }

    public void destroyView() {
        this.patternMasks.clear();
        this.nativeInventory.clear();
    }

    public void handleComponentClick(InventoryClickEvent clickEvent){

    }

    public ELDGContext getEldgContext() {
        return eldgContext;
    }

    public Map<Character, List<Integer>> getPatternMasks() {
        return patternMasks;
    }

    private View<T> initializeView(Class<View<T>> viewCls) {
        try {
            return viewCls.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("error while creating view. (view must be no-arg constructor)", e);
        }
    }

    private void renderFromTemplate(InventoryTemplate demoInventories, ItemStackService itemStackService) {
        this.patternMasks.clear();
        this.cancelMovePatterns.clear();
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
                cancelMovePatterns.add(pattern.charAt(0));
            }
        }
    }

    public final class ELDGViewContext implements ViewContext {

        @Override
        public PatternComponentBuilder pattern(char pattern) {
            return new PatternComponentFactory(pattern);
        }

        @Override
        public <F extends ComponentFactory<F>> F factory(Class<F> factoryCls) {
            return null;
        }

        private class PatternComponentFactory implements PatternComponentBuilder {

            private final char pattern;

            private PatternComponentFactory(char pattern) {
                this.pattern = pattern;
            }


            @Override
            public PatternComponentBuilder fill(Component component) {
                eldgContext.fillItem(pattern, component.getItem());
                return this;
            }

            @Override
            public PatternComponentBuilder components(Component... components) {
                for (Component component : components) {
                    eldgContext.addItem(pattern, component.getItem());
                }
                return this;
            }

            @Override
            public PatternComponentBuilder component(int pos, Component component) {
                eldgContext.setItem(pattern, pos, component.getItem());
                return this;
            }

            @Override
            public ViewContext and() {
                return ELDGViewContext.this;
            }
        }
    }


    public final class ELDGContext implements UIContext {


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

        @SuppressWarnings("unchecked")
        private <C> PersistentDataType<C, C> getPersistentDataType(Class<C> type) {
            return (PersistentDataType<C, C>) Optional.ofNullable(PERSISTENT_DATA_TYPE_MAP.get(type)).orElseThrow(() -> new IllegalStateException("Unsupported data type: " + type));
        }

        public <C> C getAttribute(Class<C> type, ItemStack itemStack, String key) {
            var meta = itemStack.getItemMeta();
            if (meta == null)
                throw new IllegalStateException("cannot get attribute: " + key + ", this item has no item meta.");
            var container = meta.getPersistentDataContainer();
            var o = getPersistentDataType(type);
            return container.get(new NamespacedKey(ELDGPlugin.getPlugin(ELDGPlugin.class), key), o);
        }

        public <C> void setAttribute(Class<C> type, ItemStack itemStack, String key, C value) {
            var meta = itemStack.getItemMeta();
            if (meta == null)
                throw new IllegalStateException("cannot get attribute: " + key + ", this item has no item meta.");
            var container = meta.getPersistentDataContainer();
            var o = getPersistentDataType(type);
            container.set(new NamespacedKey(ELDGPlugin.getPlugin(ELDGPlugin.class), key), o, value);
            itemStack.setItemMeta(meta);
        }

        @Override
        public <C> void setAttribute(Class<C> type, char pattern, String key, C value) {
            getItems(pattern).forEach(item -> this.setAttribute(type, item, key, value));
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
