package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.services.ConfigPoolService;
import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eld.services.ScheduleService;
import com.ericlam.mc.eldgui.component.AttributeController;
import com.ericlam.mc.eldgui.controller.*;
import com.ericlam.mc.eldgui.event.*;
import com.ericlam.mc.eldgui.exception.ExceptionViewHandler;
import com.ericlam.mc.eldgui.exception.HandleException;
import com.ericlam.mc.eldgui.lifecycle.PostConstruct;
import com.ericlam.mc.eldgui.lifecycle.PreDestroy;
import com.ericlam.mc.eldgui.view.BukkitRedirectView;
import com.ericlam.mc.eldgui.view.BukkitView;
import com.ericlam.mc.eldgui.view.LoadingView;
import com.ericlam.mc.eldgui.view.View;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ELDGUI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ELDGUI.class);


    private final Map<Class<? extends InventoryEvent>, ELDGEventHandler<? extends Annotation, ? extends InventoryEvent>> eventHandlerMap = new ConcurrentHashMap<>();
    private final Map<String, Function<InventoryEvent, ItemStack>> itemGetterMap = new ConcurrentHashMap<>();
    private final ELDGPlugin eldgPlugin = ELDGPlugin.getPlugin(ELDGPlugin.class);

    private final LifeCycleManager lifeCycleManager;

    private final Class<?> controllerCls;
    private final Object controller;
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
    private final BukkitView<? extends LoadingView, Void> loadingView;


    private ELDGView<?> currentView;

    public ELDGUI(
            Object controller,
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
        this.controller = controller;
        this.injector = injector;
        this.owner = owner;
        this.onDestroy = onDestroy;
        this.goTo = goTo;
        this.eldgmvcInstallation = eldgmvcInstallation;
        this.configPoolService = configPoolService;
        this.itemStackService = itemStackService;

        methodParseManager = managerFactory.buildParseManager(this::initMethodParseManager);
        returnTypeManager = managerFactory.buildReturnTypeManager(this::initReturnTypeManager);
        this.lifeCycleManager = new LifeCycleManager(controller, methodParseManager);

        var customQualifier = eldgmvcInstallation.getQualifierMap();
        this.eventHandlerMap.put(InventoryClickEvent.class, new ELDGClickEventHandler(controller, methodParseManager, returnTypeManager, customQualifier));
        this.eventHandlerMap.put(InventoryDragEvent.class, new ELDGDragEventHandler(controller, methodParseManager, returnTypeManager, customQualifier));
        this.itemGetterMap.put(InventoryClickEvent.class.getSimpleName(), e -> ((InventoryClickEvent) e).getCurrentItem());
        this.itemGetterMap.put(InventoryDragEvent.class.getSimpleName(), e -> ((InventoryDragEvent) e).getOldCursor());


        this.controllerCls = controller.getClass();

        this.lifeCycleManager.onLifeCycle(PostConstruct.class);

        Optional<Class<? extends LoadingView>> loadingViewOpt = Optional.ofNullable(this.controllerCls.getAnnotation(AsyncLoadingView.class)).map(AsyncLoadingView::value);
        var loadingView = loadingViewOpt.isPresent() ? loadingViewOpt.get() : eldgmvcInstallation.getDefaultLoadingView();
        this.loadingView = new BukkitView<>(loadingView);

        this.initIndexView(controller);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private synchronized void updateView(BukkitView<?, ?> view) {
        LOGGER.debug("update view to " + view.getView().getSimpleName()); // debug
        if (currentView != null) {
            if (controller instanceof ViewLifeCycleHook) {
                ((ViewLifeCycleHook) controller).preViewDestroy(owner, (Class<View<?>>) currentView.getView().getClass(), session);
            }
            currentView.destroyView();
        }
        currentView = new ELDGView(view, configPoolService, itemStackService, eldgmvcInstallation.getComponentFactoryMap());
        if (controller instanceof ViewLifeCycleHook) {
            ((ViewLifeCycleHook) controller).postUpdateView(owner, (Class<View<?>>) view.getView(), session);
        }
        owner.openInventory(currentView.getNativeInventory());
    }

    private synchronized void jumpToController(BukkitRedirectView redirectView) {
        if (redirectView.isCustomTransition()) {
            this.updateView(redirectView);
        }
        Bukkit.getScheduler().runTask(eldgPlugin, () -> {
            try {
                LOGGER.debug("jump tp another controller: " + redirectView.getRedirectTo()); // debug
                this.destroy();
                goTo.onJump(session, owner, redirectView.getRedirectTo());
            } catch (UINotFoundException e) {
                owner.sendMessage(e.getMessage());
            }
        });
    }

    public void initIndexView(Object controller) {
        LOGGER.debug("initializing index view"); // debug
        try {
            Optional<Method> indexMethod = Arrays.stream(controllerCls.getDeclaredMethods()).filter(m -> m.getName().equalsIgnoreCase("index")).findAny();
            if (indexMethod.isEmpty())
                throw new IllegalStateException("cannot find index method from " + controllerCls);
            Method index = indexMethod.get();
            if (index.getGenericReturnType() == Void.class || index.getGenericReturnType() == void.class)
                throw new IllegalStateException("index method cannot return void");
            Object[] objects = methodParseManager.getMethodParameters(index, null);
            Object result = index.invoke(controller, objects);
            if (!returnTypeManager.handleReturnResult(index, result)) {
                throw new IllegalStateException("cannot initialize index view for controller: " + controller);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void initReturnTypeManager(ReturnTypeManager returnTypeManager) {
        returnTypeManager.registerReturnType(type -> type.equals(new TypeLiteral<BukkitView<?, ?>>() {
        }.getType()) || type == BukkitView.class, (bukkitView, a) -> {
            if (bukkitView instanceof BukkitRedirectView) {
                this.jumpToController((BukkitRedirectView) bukkitView);
            } else {
                BukkitView<?, ?> bv = (BukkitView<?, ?>) bukkitView;
                this.updateView(bv);
            }
        });
        returnTypeManager.registerReturnType(t -> {
            if (t instanceof ParameterizedType) {
                var parat = (ParameterizedType) t;
                var inside = parat.getActualTypeArguments()[0];
                return (inside == BukkitView.class || inside.equals(new TypeLiteral<BukkitView<?, ?>>() {
                }.getType()))
                        && parat.getRawType() == ScheduleService.BukkitPromise.class;
            }
            return false;
        }, (result, annos) -> {
            // loading view start
            Arrays.stream(annos)
                    .filter(a -> a.annotationType() == AsyncLoadingView.class)
                    .findAny()
                    .map(a -> ((AsyncLoadingView) a).value())
                    .map(BukkitView::new)
                    .ifPresentOrElse(this::updateView, () -> this.updateView(this.loadingView));
            ScheduleService.BukkitPromise<BukkitView<?, ?>> promise = (ScheduleService.BukkitPromise<BukkitView<?, ?>>) result;
            promise.thenRunSync(bukkitView -> {
                if (bukkitView instanceof BukkitRedirectView) {
                    this.jumpToController((BukkitRedirectView) bukkitView);
                } else {
                    this.updateView(bukkitView);
                }
            }).join();
        });
        returnTypeManager.registerReturnType(type -> type == void.class, (view, a) -> {
        });
    }

    private void initMethodParseManager(MethodParseManager parser) {
        parser.registerParser((t, annos) -> t.equals(UISession.class), (annotations, t, e) -> session);
        parser.registerParser((t, annos) -> Arrays.stream(annos).anyMatch(a -> a.annotationType() == FromPattern.class), (annotations, t, e) -> {
            FromPattern pattern = (FromPattern) Arrays.stream(annotations).filter(a -> a.annotationType() == FromPattern.class).findAny().orElseThrow(() -> new IllegalStateException("cannot find @FromPattern in List<ItemStack> parameters"));
            if (t instanceof ParameterizedType) {
                var parat = (ParameterizedType) t;
                if (parat.getActualTypeArguments()[0] == ItemStack.class && parat.getRawType() == List.class){
                    return this.currentView.getEldgContext().getItems(pattern.value());
                }
            }
            throw new IllegalStateException("@FromPattern 必須使用 List<ItemStack> 作為其類型");
        });
        //parser.registerParser((t, annos) -> t.equals(UIRequest.class), (annotations, t, e) -> eldgContext);
        parser.registerParser((t, annos) -> t.equals(Player.class), (annotations, t, e) -> owner);
        parser.registerParser((t, annos) -> t.equals(ItemStack.class), (anno, t, e) -> getItemByEvent(e));
        parser.registerParser((t, annos) -> Arrays.stream(annos).anyMatch(a -> a.annotationType() == ItemAttribute.class), (annos, t, e) -> {
            var item = getItemByEvent(e);
            ItemAttribute attribute = (ItemAttribute) Arrays.stream(annos).filter(a -> a.annotationType() == ItemAttribute.class).findAny().orElseThrow(() -> new IllegalStateException("cannot find @ItemAttribute"));
            return this.currentView.getEldgContext().getAttribute(item, attribute.value());
        });
        parser.registerParser((t, annos) -> t instanceof Class && InventoryInteractEvent.class.isAssignableFrom((Class<?>) t), (anno, t, e) -> e);
        parser.registerParser((t, annos) -> Arrays.stream(annos).anyMatch(a -> a.annotationType() == ModelAttribute.class),
                (annotations, type, event) -> {
                    ModelAttribute modelAttribute = (ModelAttribute) Arrays.stream(annotations).filter(a -> a.annotationType() == ModelAttribute.class).findAny().orElseThrow(() -> new IllegalStateException("cannot find @ModelAttribute"));
                    var context = this.currentView.getEldgContext();
                    if (type instanceof ParameterizedType)
                        throw new IllegalStateException("model attribute cannot be generic type");
                    var model = ((Class<?>) type);

                    Map<String, Object> fieldMap = context.getItems(modelAttribute.value())
                            .stream()
                            .filter(item -> context.getAttribute(item, AttributeController.FIELD_TAG) != null)
                            .collect(Collectors
                                    .toMap(
                                            item -> context.getAttribute(item, AttributeController.FIELD_TAG),
                                            item -> Optional.ofNullable(context.getAttribute(item, AttributeController.VALUE_TAG)).orElseThrow(() -> new IllegalStateException("The value tag of " + item.toString() + " is null."))
                                    )
                            );
                    Map<String, Object> toConvert = PersistDataUtils.toNestedMap(fieldMap);
                    LOGGER.debug("using " + toConvert + " to create instance of " + model);
                    return PersistDataUtils.mapToObject(toConvert, model);
                });
        parser.registerParser((t, annos) -> Arrays.stream(annos).anyMatch(a -> a.annotationType() == MapAttribute.class),
                (annotations, type, event) -> {
                    MapAttribute attribute = (MapAttribute) Arrays.stream(annotations).filter(a -> a.annotationType() == MapAttribute.class).findAny().orElseThrow(() -> new IllegalStateException("cannot find MapAttribute annotation"));
                    var context = this.currentView.getEldgContext();
                    boolean isMap = false;
                    if (type instanceof ParameterizedType){
                        var parat = (ParameterizedType)type;
                        isMap = parat.getRawType() == Map.class && parat.getActualTypeArguments()[0] == String.class && parat.getActualTypeArguments()[1] == Object.class;
                    }

                    if (!isMap) throw new IllegalStateException("@MapAttribute 必須使用 Map<String, Object> 作為其類型");
                    Map<String, Object> fieldMap = context.getItems(attribute.value())
                            .stream()
                            .filter(item -> context.getAttribute(item, AttributeController.FIELD_TAG) != null)
                            .collect(Collectors
                                    .toMap(
                                            item -> context.getAttribute(item, AttributeController.FIELD_TAG),
                                            item -> Optional.ofNullable(context.getAttribute(item, AttributeController.VALUE_TAG)).orElseThrow(() -> new IllegalStateException("The value tag of " + item.toString() + " is null."))
                                    )
                            );
                    return PersistDataUtils.toNestedMap(fieldMap);
                });
    }

    private ItemStack getItemByEvent(InventoryEvent e) {
        return Optional.ofNullable(e).map(ee -> itemGetterMap.get(ee.getEventName())).map(f -> f.apply(e)).orElseThrow(() -> new IllegalStateException("no item return by the event or the event is null"));
    }


    @SuppressWarnings("unchecked")
    public void onInventoryClick(InventoryClickEvent e) {
        if (this.currentView == null) return;
        LOGGER.debug("on Inventory Click"); // debug
        if (!this.currentView.handleComponentClick(e)) return;
        // pass to controller
        var handler = (ELDGEventHandler<? extends Annotation, InventoryClickEvent>) eventHandlerMap.get(e.getClass());
        if (handler == null) return;
        try {
            handler.onEventHandle(e, owner, this.currentView);
        } catch (Exception ex) {
            this.handleException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public void onInventoryDrag(InventoryDragEvent e) {
        if (this.currentView == null) return;
        LOGGER.debug("on inventory drag"); // debug
        var handler = (ELDGEventHandler<? extends Annotation, InventoryDragEvent>) eventHandlerMap.get(e.getClass());
        if (handler == null) return;
        try {
            handler.onEventHandle(e, owner, this.currentView);
        } catch (Exception ex) {
            this.handleException(ex);
        }
    }

    public void onInventoryOpen(InventoryOpenEvent e) {
        if (this.currentView == null) return;
        LOGGER.debug("on inventory open"); // debug
        if (this.currentView.getNativeInventory() == e.getInventory()) return;
        destroy();
    }

    private void handleException(Exception ex) {
        LOGGER.debug("on exception handle: " + ex.getClass()); // debug
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
                                returnTypeManager.handleReturnResult(method, returnObject);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        },
                        () -> returnTypeManager.handleReturnResult(BukkitView.class,
                                viewHandlerIns.createErrorView(ex, fromController.value(), session, owner)
                        ));
    }

    public void onInventoryClose(InventoryCloseEvent e) {
        if (this.currentView == null) return;
        if (e.getPlayer() != this.owner) return;
        if (e.getInventory() != this.currentView.getNativeInventory()) return;
        if (this.currentView.isDoNotDestroyView()) return;
        LOGGER.debug("on inventory close"); // debug
        destroy();
    }

    // controller destroy
    public synchronized void destroy() {
        LOGGER.debug("destroying controller"); //debug
        eventHandlerMap.values().forEach(ELDGEventHandler::unloadAllHandlers);
        lifeCycleManager.onLifeCycle(PreDestroy.class);
        this.currentView.destroyView();
        this.onDestroy.accept(owner);
    }


}
