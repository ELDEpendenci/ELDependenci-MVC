package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.services.ConfigPoolService;
import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eld.services.ScheduleService;
import com.ericlam.mc.eldgui.controller.FromPattern;
import com.ericlam.mc.eldgui.controller.ItemAttribute;
import com.ericlam.mc.eldgui.event.ELDGEventHandler;
import com.ericlam.mc.eldgui.event.MethodParseManager;
import com.ericlam.mc.eldgui.event.ReturnTypeManager;
import com.ericlam.mc.eldgui.event.click.ELDGClickEventHandler;
import com.ericlam.mc.eldgui.event.drag.ELDGDragEventHandler;
import com.ericlam.mc.eldgui.view.BukkitRedirectView;
import com.ericlam.mc.eldgui.view.BukkitView;
import com.google.inject.Injector;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ELDGUI implements Listener {


    private final UISession session;
    private final Map<Character, List<Integer>> patternMasks = new LinkedHashMap<>();
    //private final ItemStackService itemStackService;
    private final Player owner;
    private final Map<Class<? extends InventoryEvent>, ELDGEventHandler<? extends Annotation, ? extends InventoryEvent>> eventHandlerMap = new ConcurrentHashMap<>();
    private final Map<String, Function<InventoryEvent, ItemStack>> itemGetterMap = new ConcurrentHashMap<>();
    //private final Class<? extends Model> modelClass;
    private final Consumer<Player> onDestroy;
    private final ViewJumper goTo;
    //private final LifeCycleManager lifeCycleManager;


    private final Class<?> controllerCls;
    private final MethodParseManager methodParseManager;
    private final ReturnTypeManager returnTypeManager;

    @Inject
    private ELDGPlugin eldgPlugin;
    @Inject
    private Injector injector;
    @Inject
    private ConfigPoolService configPoolService;
    @Inject
    private ItemStackService itemStackService;

    private ELDGView<?> currentView;
    private ELDGView<?>.ELDGContext eldgContext;

    public ELDGUI(Object controller,
                  UISession session,
                  Player owner,
                  ManagerFactory managerFactory,
                  Consumer<Player> onDestroy,
                  ViewJumper goTo
    ) {

        this.session = session;
        this.owner = owner;
        this.onDestroy = onDestroy;
        this.goTo = goTo;

        methodParseManager = managerFactory.buildParseManager(this::initMethodParseManager);
        returnTypeManager = managerFactory.buildReturnTypeManager(this::initReturnTypeManager);
        //this.lifeCycleManager = new LifeCycleManager(controller, methodParseManager);


        this.eventHandlerMap.put(InventoryClickEvent.class, new ELDGClickEventHandler(controller, methodParseManager, returnTypeManager));
        this.eventHandlerMap.put(InventoryDragEvent.class, new ELDGDragEventHandler(controller, methodParseManager, returnTypeManager));
        this.itemGetterMap.put(InventoryClickEvent.class.getSimpleName(), e -> ((InventoryClickEvent) e).getCurrentItem());
        this.itemGetterMap.put(InventoryDragEvent.class.getSimpleName(), e -> ((InventoryDragEvent) e).getOldCursor());


        this.controllerCls = controller.getClass();
        this.initIndexView();
        Bukkit.getServer().getPluginManager().registerEvents(this, ELDGPlugin.getPlugin(ELDGPlugin.class));
    }

    private void updateView(BukkitView<?, ?> view) {
        owner.closeInventory();
        Bukkit.getScheduler().runTask(eldgPlugin, () -> {
            currentView = new ELDGView(view, configPoolService, itemStackService);
            eldgContext = currentView.getEldgContext();
            owner.openInventory(currentView.getNativeInventory());
        });
    }

    private void jumpToController(BukkitRedirectView redirectView){
       if (redirectView.isCustomTransition()){
           this.updateView(redirectView);
       }
       Bukkit.getScheduler().runTask(eldgPlugin, () -> {
           try {
               goTo.onJump(session, owner, redirectView.getRedirectTo());
           } catch (UINotFoundException e) {
               owner.sendMessage(e.getMessage());
           }
       });
    }

    public void initIndexView() {
        try {
            Optional<Method> indexMethod = Arrays.stream(controllerCls.getDeclaredMethods()).filter(m -> m.getName().equalsIgnoreCase("index")).findAny();
            if (indexMethod.isEmpty())
                throw new IllegalStateException("cannot find index method from " + controllerCls);
            Method index = indexMethod.get();
            Object[] objects = methodParseManager.getMethodParameters(index, null);
            returnTypeManager.handleReturnResult(index.getReturnType(), index.invoke(index, objects));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void initReturnTypeManager(ReturnTypeManager returnTypeManager) {
        returnTypeManager.registerReturnType(type -> type == BukkitView.class, bukkitView -> {
            if (bukkitView instanceof BukkitRedirectView) {
                this.jumpToController((BukkitRedirectView) bukkitView);
            } else {
                BukkitView<?, ?> bv = (BukkitView<?, ?>) bukkitView;
                this.updateView(bv);
            }
        });
        returnTypeManager.registerReturnType(type -> type == void.class, view -> {
        });
    }

    private void initMethodParseManager(MethodParseManager parser) {
        parser.registerParser((t, annos) -> t.equals(UISession.class), (annotations, t, e) -> session);
        parser.registerParser((t, annos) -> {
            if (t instanceof ParameterizedType) {
                var parat = (ParameterizedType) t;
                return parat.getActualTypeArguments()[0] == ItemStack.class && parat.getRawType() == List.class;
            }
            return false;
        }, (annotations, t, e) -> {
            FromPattern pattern = (FromPattern) Arrays.stream(annotations).filter(a -> a.annotationType() == FromPattern.class).findAny().orElseThrow(() -> new IllegalStateException("cannot find @FromPattern in List<ItemStack> parameters"));
            if (pattern.fromDrag() && e instanceof InventoryDragEvent) {
                Map<Integer, ItemStack> map = ((InventoryDragEvent) e).getNewItems();
                return map.entrySet().stream().filter(en -> Optional.ofNullable(patternMasks.get(pattern.value())).map(list -> list.contains(en.getKey())).orElse(false)).map(Map.Entry::getValue).collect(Collectors.toList());
            }
            return eldgContext.getItems(pattern.value());
        });
        //parser.registerParser((t, annos) -> t.equals(UIRequest.class), (annotations, t, e) -> eldgContext);
        parser.registerParser((t, annos) -> t.equals(Player.class), (annotations, t, e) -> owner);
        parser.registerParser((t, annos) -> t.equals(ItemStack.class), (anno, t, e) -> getItemByEvent(e));
        parser.registerParser((t, annos) -> Arrays.stream(annos).anyMatch(a -> a.annotationType() == ItemAttribute.class), (annos, t, e) -> {
            var item = getItemByEvent(e);
            ItemAttribute attribute = (ItemAttribute) Arrays.stream(annos).filter(a -> a.annotationType() == ItemAttribute.class).findAny().orElseThrow(() -> new IllegalStateException("cannot find @ItemAttribute"));
            return eldgContext.getAttribute((Class<?>) t, item, attribute.value());
        });
        parser.registerParser((t, annos) -> t instanceof Class && InventoryInteractEvent.class.isAssignableFrom((Class<?>) t), (anno, t, e) -> e);
    }

    private ItemStack getItemByEvent(InventoryEvent e) {
        return Optional.ofNullable(e).map(ee -> itemGetterMap.get(ee.getEventName())).map(f -> f.apply(e)).orElseThrow(() -> new IllegalStateException("no item return by the event or the event is null"));
    }


    @SuppressWarnings("unchecked")
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (this.currentView == null) return;
        var handler = (ELDGEventHandler<? extends Annotation, InventoryClickEvent>) eventHandlerMap.get(e.getClass());
        if (handler == null) return;
        handler.onEventHandle(e,
                owner,
                this.currentView.getNativeInventory(),
                patternMasks,
                this.currentView.getView()
        );
    }

    @SuppressWarnings("unchecked")
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (this.currentView == null) return;
        var handler = (ELDGEventHandler<? extends Annotation, InventoryDragEvent>) eventHandlerMap.get(e.getClass());
        if (handler == null) return;
        handler.onEventHandle(e,
                owner,
                this.currentView.getNativeInventory(),
                patternMasks,
                this.currentView.getView()
        );
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getPlayer() != this.owner) return;
        if (e.getInventory() != this.currentView.getNativeInventory()) return;
        destroy();
    }

    public void destroy() {
        eventHandlerMap.values().forEach(ELDGEventHandler::unloadAllHandlers);
        HandlerList.unregisterAll(this);
        //lifeCycleManager.onLifeCycle(OnDestroy.class);
        patternMasks.clear();
        this.currentView.getNativeInventory().clear();
        this.onDestroy.accept(owner);
    }


}
