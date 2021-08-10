package com.ericlam.mc.eldgui.event;

import com.ericlam.mc.eldgui.view.BukkitRedirectView;
import com.ericlam.mc.eldgui.view.View;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ELDGEventHandler<A extends Annotation, E extends InventoryEvent> {

    protected final Map<RequestMapping, Method> eventMap = new ConcurrentHashMap<>();
    private Set<Character> cancellable = new HashSet<>();
    private final Object uiController;
    private final MethodParseManager parseManager;
    private final ReturnTypeManager returnTypeManager;


    public ELDGEventHandler(Object controller, MethodParseManager parseManager, ReturnTypeManager returnTypeManager) {
        this.uiController = controller;
        this.parseManager = parseManager;
        this.returnTypeManager = returnTypeManager;
        this.loadAllCommonHandlers(controller);
        this.loadAllHandlers(controller).forEach((k, v) -> eventMap.put(toRequestMapping(k), v));
    }

    private void loadAllCommonHandlers(Object controller) {
        Arrays.stream(controller.getClass().getDeclaredMethods()).parallel()
                .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                .forEach(m -> eventMap.put(m.getAnnotation(RequestMapping.class), m));
    }

    protected abstract Map<A, Method> loadAllHandlers(Object controller);

    public void unloadAllHandlers() {
        eventMap.clear();
    }

    public void setCancellable(Set<Character> cancellable) {
        this.cancellable = cancellable;
    }

    public void onEventHandle(
            E e,
            Player player,
            Inventory nativeInventory,
            Map<Character, List<Integer>> patternMasks,
            View<?> currentView
    ) {
        if (e instanceof InventoryInteractEvent) {
            if (((InventoryInteractEvent) e).getWhoClicked() != player) return;
        }
        if (!e.getViewers().contains(player)) return;
        if (e.getInventory() != nativeInventory) return;
        Optional<Character> chOpt = patternMasks.keySet().stream().filter(ch -> slotTrigger(patternMasks.get(ch), e)).findAny();
        if (chOpt.isEmpty()) return;
        final var patternClicked = chOpt.get();
        if (cancellable.contains(patternClicked) && e instanceof Cancellable) {
            ((Cancellable) e).setCancelled(true);
        }
        eventMap.entrySet()
                .parallelStream()
                .filter((en) -> {
                    var requestMapper = en.getKey();
                    if (e instanceof Cancellable && ((Cancellable) e).isCancelled() && requestMapper.ignoreCancelled()) {
                        return false;
                    }
                    return requestMapper.pattern() == patternClicked && requestMapper.event() == e.getClass();
                })
                .sorted(Comparator.comparingInt(m -> m.getKey().order()))
                .map(Map.Entry::getValue)
                .forEachOrdered(m -> {
                    try {
                        Object[] results = parseManager.getMethodParameters(m, e);
                        var returnType = m.invoke(uiController, results);
                        returnTypeManager.handleReturnResult(m.getGenericReturnType(), returnType);
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                        player.sendMessage("§c執行界面事件時發生錯誤，請聯絡插件師");
                        ex.printStackTrace();
                    }
                });
    }


    protected abstract boolean slotTrigger(List<Integer> slots, E event);

    protected abstract RequestMapping toRequestMapping(A annotation);

}
