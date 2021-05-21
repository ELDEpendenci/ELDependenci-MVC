package com.ericlam.mc.eldgui.event;

import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.model.Model;
import org.bukkit.Bukkit;
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

    protected final Map<A, Method> eventMap = new ConcurrentHashMap<>();
    private final Set<Character> cancellable = new HashSet<>();
    private final UIController uiController;
    private final MethodParseManager<E> parseManager;
    private final ReturnTypeManager returnTypeManager;


    public ELDGEventHandler(UIController controller, MethodParseManager<E> parseManager, ReturnTypeManager returnTypeManager) {
        this.uiController = controller;
        this.parseManager = parseManager;
        this.returnTypeManager = returnTypeManager;
        this.loadAllHandlers(controller);
    }

    protected abstract void loadAllHandlers(UIController controller);

    public void unloadAllHandlers() {
        eventMap.clear();
    }

    public void addCancel(char c) {
        this.cancellable.add(c);
    }

    public void onEventHandle(
            E e,
            Player player,
            Inventory nativeInventory,
            Map<Character, List<Integer>> patternMasks
    ){
        if (e instanceof InventoryInteractEvent){
            if (((InventoryInteractEvent) e).getWhoClicked() != player) return;
        }
        if (!e.getViewers().contains(player)) return;
        if (e.getInventory() != nativeInventory) return;
        Optional<Character> chOpt = patternMasks.keySet().stream().filter(ch -> Arrays.binarySearch(patternMasks.get(ch).toArray(new Integer[0]), getSlotFromEvent(e)) >= 0).findAny();
        if (chOpt.isEmpty()) return;
        final var patternClicked = chOpt.get();
        if (cancellable.contains(patternClicked) && e instanceof Cancellable) {
            ((Cancellable) e).setCancelled(true);
        }
        eventMap.entrySet()
                .parallelStream()
                .filter((en) -> {
                    var annotate = en.getKey();
                    var baseHandler = getBaseHandler(annotate);
                    if (e instanceof Cancellable && ((Cancellable) e).isCancelled() && baseHandler.ignoreCancelled()) {
                        return false;
                    }
                    var insidePattern = baseHandler.patterns().length == 0 || Arrays.binarySearch(baseHandler.patterns(), patternClicked) >= 0;
                    return insidePattern && annotationFilter(annotate, e);
                })
                .sorted(Comparator.comparingInt(m -> getBaseHandler(m.getKey()).order()))
                .map(Map.Entry::getValue)
                .forEachOrdered(m -> {
                    try {
                        Object[] results = parseManager.getMethodParameters(m);
                        var returnType = m.invoke(uiController, results);
                        returnTypeManager.handleReturnResult(m.getGenericReturnType(), returnType);
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                        player.sendMessage("§c執行界面事件時發生錯誤，請聯絡插件師");
                        ex.printStackTrace();
                    }
                });
    }

    protected abstract int getSlotFromEvent(E event);

    protected abstract boolean annotationFilter(A annotate, E event);

    protected BaseHandler getBaseHandler(A annotation){
        try {
            Method m = annotation.getClass().getMethod("base");
            return (BaseHandler) m.invoke(annotation);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("annotation handler "+annotation.getClass()+" is lack of base = @BaseHandler property.", e);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}
