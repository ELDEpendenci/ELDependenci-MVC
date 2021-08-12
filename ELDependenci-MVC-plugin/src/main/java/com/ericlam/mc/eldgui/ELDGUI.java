package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.services.ConfigPoolService;
import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.controller.FromPattern;
import com.ericlam.mc.eldgui.controller.ItemAttribute;
import com.ericlam.mc.eldgui.controller.ModelAttribute;
import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.event.*;
import com.ericlam.mc.eldgui.exception.ExceptionViewHandler;
import com.ericlam.mc.eldgui.exception.HandleException;
import com.ericlam.mc.eldgui.view.BukkitRedirectView;
import com.ericlam.mc.eldgui.view.BukkitView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ELDGUI implements Listener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ELDGUI.class);

    private final Map<Class<? extends InventoryEvent>, ELDGEventHandler<? extends Annotation, ? extends InventoryEvent>> eventHandlerMap = new ConcurrentHashMap<>();
    private final Map<String, Function<InventoryEvent, ItemStack>> itemGetterMap = new ConcurrentHashMap<>();
    private final ELDGPlugin eldgPlugin = ELDGPlugin.getPlugin(ELDGPlugin.class);

    //private final LifeCycleManager lifeCycleManager;

    private final Class<?> controllerCls;
    private final Injector injector;
    private final UISession session;
    private final Player owner;
    private final MethodParseManager methodParseManager;
    private final ReturnTypeManager returnTypeManager;
    private final ELDGMVCInstallation eldgmvcInstallation;
    private final ConfigPoolService configPoolService;
    private final ItemStackService itemStackService;
    private final Consumer<Player> onDestroy;
    private final ViewJumper goTo;

    private ELDGView<?> currentView;

    public ELDGUI(Object controller,
                  Injector injector,
                  UISession session,
                  Player owner,
                  ManagerFactory managerFactory,
                  Consumer<Player> onDestroy,
                  ViewJumper goTo,
                  ELDGMVCInstallation eldgmvcInstallation,
                  ConfigPoolService configPoolService,
                  ItemStackService itemStackService
    ) {

        this.session = session;
        this.injector = injector;
        this.owner = owner;
        this.onDestroy = onDestroy;
        this.goTo = goTo;
        this.eldgmvcInstallation = eldgmvcInstallation;
        this.configPoolService = configPoolService;
        this.itemStackService = itemStackService;

        methodParseManager = managerFactory.buildParseManager(this::initMethodParseManager);
        returnTypeManager = managerFactory.buildReturnTypeManager(this::initReturnTypeManager);
        //this.lifeCycleManager = new LifeCycleManager(controller, methodParseManager);

        var customQualifier = eldgmvcInstallation.getQualifierMap();
        this.eventHandlerMap.put(InventoryClickEvent.class, new ELDGClickEventHandler(controller, methodParseManager, returnTypeManager, customQualifier));
        this.eventHandlerMap.put(InventoryDragEvent.class, new ELDGDragEventHandler(controller, methodParseManager, returnTypeManager, customQualifier));
        this.itemGetterMap.put(InventoryClickEvent.class.getSimpleName(), e -> ((InventoryClickEvent) e).getCurrentItem());
        this.itemGetterMap.put(InventoryDragEvent.class.getSimpleName(), e -> ((InventoryDragEvent) e).getOldCursor());


        this.controllerCls = controller.getClass();
        this.initIndexView(controller);
        Bukkit.getServer().getPluginManager().registerEvents(this, eldgPlugin);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private synchronized void updateView(BukkitView<?, ?> view) {
        LOGGER.info("update view to " + view.getView().getSimpleName()); // debug
        if (currentView != null) currentView.destroyView();
        currentView = new ELDGView(view, configPoolService, itemStackService);
        owner.openInventory(currentView.getNativeInventory());
    }

    private synchronized void jumpToController(BukkitRedirectView redirectView) {
        if (redirectView.isCustomTransition()) {
            this.updateView(redirectView);
        }
        Bukkit.getScheduler().runTask(eldgPlugin, () -> {
            try {
                LOGGER.info("jump tp another controller: " + redirectView.getRedirectTo()); // debug
                goTo.onJump(session, owner, redirectView.getRedirectTo());
            } catch (UINotFoundException e) {
                owner.sendMessage(e.getMessage());
            }
        });
    }

    public void initIndexView(Object controller) {
        LOGGER.info("initializing index view"); // debug
        try {
            Optional<Method> indexMethod = Arrays.stream(controllerCls.getDeclaredMethods()).filter(m -> m.getName().equalsIgnoreCase("index")).findAny();
            if (indexMethod.isEmpty())
                throw new IllegalStateException("cannot find index method from " + controllerCls);
            Method index = indexMethod.get();
            Object[] objects = methodParseManager.getMethodParameters(index, null);
            Object result = index.invoke(controller, objects);
            if (!(result instanceof BukkitView))
                throw new IllegalStateException("index method must return bukkit view");
            if (!returnTypeManager.handleReturnResult(index.getGenericReturnType(), result)) {
                throw new IllegalStateException("cannot initialize index view for controller: " + controller);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initReturnTypeManager(ReturnTypeManager returnTypeManager) {
        returnTypeManager.registerReturnType(type -> type.equals(new TypeLiteral<BukkitView<?, ?>>() {
        }.getType()) || type == BukkitView.class, bukkitView -> {
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
            return this.currentView.getEldgContext().getItems(pattern.value());
        });
        //parser.registerParser((t, annos) -> t.equals(UIRequest.class), (annotations, t, e) -> eldgContext);
        parser.registerParser((t, annos) -> t.equals(Player.class), (annotations, t, e) -> owner);
        parser.registerParser((t, annos) -> t.equals(ItemStack.class), (anno, t, e) -> getItemByEvent(e));
        parser.registerParser((t, annos) -> Arrays.stream(annos).anyMatch(a -> a.annotationType() == ItemAttribute.class), (annos, t, e) -> {
            var item = getItemByEvent(e);
            ItemAttribute attribute = (ItemAttribute) Arrays.stream(annos).filter(a -> a.annotationType() == ItemAttribute.class).findAny().orElseThrow(() -> new IllegalStateException("cannot find @ItemAttribute"));
            return this.currentView.getEldgContext().getAttribute((Class<?>) t, item, attribute.value());
        });
        parser.registerParser((t, annos) -> t instanceof Class && InventoryInteractEvent.class.isAssignableFrom((Class<?>) t), (anno, t, e) -> e);
        parser.registerParser((t, annos) -> Arrays.stream(annos).anyMatch(a -> a.annotationType() == ModelAttribute.class),
                (annotations, type, event) -> {
                    ModelAttribute modelAttribute = (ModelAttribute) Arrays.stream(annotations).filter(a -> a.annotationType() == ModelAttribute.class).findAny().orElseThrow(() -> new IllegalStateException("cannot find @ModelAttribute"));
                    var context = this.currentView.getEldgContext();
                    List<ItemStack> items = context.getItems(modelAttribute.value());
                    if (type instanceof ParameterizedType)
                        throw new IllegalStateException("model attribute cannot be generic type");
                    var model = ((Class<?>) type);
                    Object modelObject;
                    try {
                        Constructor<?> con = model.getConstructor();
                        modelObject = con.newInstance();
                    } catch (NoSuchMethodException e) {
                        throw new IllegalStateException("model should have no arg constructor.", e);
                    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                        throw new IllegalStateException("error while initializing instance of model " + model, e);
                    }
                    fieldsToMap(model).forEach((fName, fType) -> {
                        Object value = null;
                        for (ItemStack item : items) {
                            value = context.getAttribute(fType, item, fName);
                            if (value != null) break;
                        }
                        if (value == null)
                            throw new IllegalStateException("cannot find " + fName + " value for model " + model);
                        try {
                            Field field = model.getField(fName);
                            field.setAccessible(true);
                            field.set(modelObject, value);
                        } catch (NoSuchFieldException e) {
                            throw new IllegalStateException("cannot find the field " + fName + " from model " + model, e);
                        } catch (IllegalAccessException e) {
                            throw new IllegalStateException("error while setting field " + fName + " with type " + fType + " from model " + model, e);
                        }
                    });
                    return modelObject;
                });
    }

    private ItemStack getItemByEvent(InventoryEvent e) {
        return Optional.ofNullable(e).map(ee -> itemGetterMap.get(ee.getEventName())).map(f -> f.apply(e)).orElseThrow(() -> new IllegalStateException("no item return by the event or the event is null"));
    }


    @SuppressWarnings("unchecked")
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (this.currentView == null) return;
        LOGGER.info("on Inventory Click"); // debug
        var handler = (ELDGEventHandler<? extends Annotation, InventoryClickEvent>) eventHandlerMap.get(e.getClass());
        if (handler == null) return;
        try {
            handler.onEventHandle(e, owner, this.currentView);
        } catch (Exception ex) {
            this.handleException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (this.currentView == null) return;
        LOGGER.info("on inventory drag"); // debug
        var handler = (ELDGEventHandler<? extends Annotation, InventoryDragEvent>) eventHandlerMap.get(e.getClass());
        if (handler == null) return;
        try {
            handler.onEventHandle(e, owner, this.currentView);
        } catch (Exception ex) {
            this.handleException(ex);
        }
    }

    private void handleException(Exception ex) {
        LOGGER.info("on exception handle: " + ex.getClass()); // debug
        Optional<Class<? extends ExceptionViewHandler>> exceptionViewHandlerOpt = Optional
                .ofNullable(eldgmvcInstallation.getExceptionHandlerMap().get(controllerCls));
        if (exceptionViewHandlerOpt.isEmpty()) {
            exceptionViewHandlerOpt = eldgmvcInstallation.getScopedExceptionHandlerSet().stream()
                    .filter(handler -> handler.getProtectionDomain().getCodeSource().hashCode() == controllerCls.getProtectionDomain().getCodeSource().hashCode())
                    .findAny();
        }
        Class<? extends ExceptionViewHandler> exceptionViewHandler = exceptionViewHandlerOpt.orElseGet(eldgmvcInstallation::getDefaultExceptionHandler);
        ExceptionViewHandler viewHandlerIns = injector.getInstance(exceptionViewHandler);
        UIController fromController = controllerCls.getAnnotation(UIController.class);
        Arrays.stream(exceptionViewHandler.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(HandleException.class))
                .filter(m -> Arrays.stream(m.getAnnotation(HandleException.class).value()).anyMatch(v -> {
                    Class<?> superCls = ex.getClass();
                    while (superCls != null) {
                        if (v == superCls) return true;
                        superCls = superCls.getSuperclass();
                    }
                    return false;
                }))
                .findFirst()
                .ifPresentOrElse(
                        method -> {
                            try {
                                Object returnObject = method.invoke(viewHandlerIns, ex, fromController.value(), session, owner);
                                if (!(returnObject instanceof BukkitView))
                                    throw new IllegalStateException("error view must return bukkit view type");
                                returnTypeManager.handleReturnResult(method.getReturnType(), returnObject);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        },
                        () -> returnTypeManager.handleReturnResult(BukkitView.class,
                                viewHandlerIns.createErrorView(ex, fromController.value(), session, owner)
                        ));
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (this.currentView == null) return;
        if (e.getPlayer() != this.owner) return;
        if (e.getInventory() != this.currentView.getNativeInventory()) return;
        LOGGER.info("on inventory close"); // debug
        destroy();
    }

    // controller destroy
    public void destroy() {
        LOGGER.info("destroying controller"); //debug
        eventHandlerMap.values().forEach(ELDGEventHandler::unloadAllHandlers);
        HandlerList.unregisterAll(this);
        //lifeCycleManager.onLifeCycle(OnDestroy.class);
        this.currentView.destroyView();
        this.onDestroy.accept(owner);
    }


    public static Map<String, Object> reflectToMap(Object model) {
        if (model == null || model.getClass().isPrimitive()) return Map.of();
        return Arrays.stream(model.getClass().getFields()).filter(f -> !f.isAnnotationPresent(JsonIgnore.class)).collect(Collectors.toMap(Field::getName, f -> {
            try {
                f.setAccessible(true);
                return f.get(model);
            } catch (Exception e) {
                e.printStackTrace();
                return "[Error: " + e.getClass().getSimpleName() + "]";
            }
        }));
    }

    public static Map<String, Class<?>> fieldsToMap(Class<?> cls) {
        return Arrays.stream(cls.getFields()).filter(f -> !f.isAnnotationPresent(JsonIgnore.class)).collect(Collectors.toMap(Field::getName, Field::getType));
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> gsonToMap(Object model) {
        if (model == null || model.getClass().isPrimitive()) return Map.of();
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(model), Map.class);
    }

}
