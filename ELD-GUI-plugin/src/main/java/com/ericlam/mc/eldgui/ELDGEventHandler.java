package com.ericlam.mc.eldgui;

import com.ericlam.mc.eldgui.event.UIClickEvent;
import com.ericlam.mc.eldgui.event.UIEvent;
import com.ericlam.mc.eldgui.event.UIHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ELDGEventHandler {

    private final Map<Class<?>, Map<UIHandler, Method>> eventMap = new ConcurrentHashMap<>();
    private final Set<Character> cancelClick = new HashSet<>();
    private final Map<Character, ELDGDispatcher> jumpTo = new HashMap<>();

    public ELDGEventHandler() {

        this.eventMap.put(InventoryClickEvent.class, new ConcurrentHashMap<>());
    }

    public void loadAllHandlers(UIRenderer renderer) {
        Arrays.stream(renderer.getClass().getDeclaredMethods()).parallel()
                .filter(m ->
                        m.isAnnotationPresent(UIHandler.class) &&
                                m.getParameterCount() == 1 &&
                                UIEvent.class.isAssignableFrom(m.getParameterTypes()[0]) &&
                                eventMap.containsKey(m.getParameterTypes()[0])
                )
                .forEach(m -> eventMap.get(m.getParameterTypes()[0]).put(m.getAnnotation(UIHandler.class), m));
    }

    public void unloadAllHandlers() {
        eventMap.values().forEach(Map::clear);
    }

    public void addCancelClick(char c) {
        this.cancelClick.add(c);
    }

    public void addJumpTo(char c, ELDGDispatcher dispatcher) {
        jumpTo.put(c, dispatcher);
    }

    public void onInventoryClick(
            InventoryClickEvent e,
            Player player,
            Inventory nativeInventory,
            Map<Character, List<Integer>> patternMasks,
            UIAction uiAction,
            InventoryScope scope
    ) {
        if (player != e.getWhoClicked()) return;
        if (nativeInventory != e.getClickedInventory()) return;
        if (e.getSlotType() != InventoryType.SlotType.CONTAINER) return;
        var clickEventMap = eventMap.get(InventoryClickEvent.class);
        char key = '\0';
        for (Character ch : patternMasks.keySet()) {
            if (Arrays.binarySearch(patternMasks.get(ch).toArray(new Integer[0]), e.getSlot()) > 0) {
                key = ch;
                break;
            }
        }
        if (key == '\0') return;
        final var patternClicked = key;
        if (cancelClick.contains(patternClicked)) {
            e.setCancelled(true);
        }
        if (jumpTo.containsKey(patternClicked)) {
            e.setCancelled(true);
            jumpTo.get(patternClicked).forward(player, scope.getSessionScope());
            return;
        }
        clickEventMap.entrySet()
                .parallelStream()
                .filter((en) -> {
                    // owner.sendMessage(e.getAction().toString()); //
                    var uiHandler = en.getKey();
                    if (e.isCancelled() && uiHandler.ignoreCancelled()) {
                        return false;
                    }
                    var insidePattern = uiHandler.patterns().length == 0 || Arrays.binarySearch(uiHandler.patterns(), patternClicked) > 0;
                    var actionEqual = uiHandler.filterActions().length == 0 || Arrays.binarySearch(uiHandler.filterActions(), e.getAction()) > 0;
                    var clickEqual = uiHandler.filterClicks().length == 0 || Arrays.binarySearch(uiHandler.filterClicks(), e.getClick()) > 0;
                    return actionEqual && clickEqual && insidePattern;
                })
                .sorted(Comparator.comparingInt(m -> m.getKey().order()))
                .map(Map.Entry::getValue)
                .forEachOrdered(m -> {
                    try {
                        var event = new UIClickEvent(player, uiAction, scope, e, patternClicked, e.getCurrentItem());
                        m.setAccessible(true);
                        m.invoke(event);
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                        player.sendMessage("§c執行界面事件時發生錯誤，請聯絡插件師");
                        ex.printStackTrace();
                    }
                });
    }
}
