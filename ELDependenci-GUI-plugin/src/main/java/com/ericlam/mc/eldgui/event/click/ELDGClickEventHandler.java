package com.ericlam.mc.eldgui.event.click;

import com.ericlam.mc.eldgui.event.*;
import com.ericlam.mc.eldgui.view.View;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ELDGClickEventHandler extends ELDGEventHandler<ClickMapping, InventoryClickEvent> {


    public ELDGClickEventHandler(Object controller, MethodParseManager parseManager, ReturnTypeManager returnTypeManager) {
        super(controller, parseManager, returnTypeManager);
    }

    @Override
    protected Map<ClickMapping, Method> loadAllHandlers(Object controller) {
        return  Arrays.stream(controller.getClass().getDeclaredMethods()).parallel()
                .filter(m -> m.isAnnotationPresent(ClickMapping.class))
                .collect(Collectors.toMap(m -> m.getAnnotation(ClickMapping.class), m -> m));
    }


    @Override
    public void onEventHandle(InventoryClickEvent e, Player player, Inventory nativeInventory, Map<Character, List<Integer>> patternMasks, View<?> currentView) {
        if (nativeInventory != e.getClickedInventory()) return;
        if (e.getSlotType() != InventoryType.SlotType.CONTAINER) return;
        super.onEventHandle(e, player, nativeInventory, patternMasks, currentView);
    }

    @Override
    protected boolean slotTrigger(List<Integer> slots, InventoryClickEvent event) {
        return slots.contains(event.getSlot());
    }

    @Override
    protected RequestMapping toRequestMapping(ClickMapping annotation) {
        return new RequestMapping(){

            @Override
            public Class<? extends View<?>> view() {
                return annotation.view();
            }

            @Override
            public Class<? extends InventoryInteractEvent> event() {
                return InventoryClickEvent.class;
            }

            @Override
            public char pattern() {
                return annotation.pattern();
            }

            @Override
            public boolean ignoreCancelled() {
                return annotation.ignoreCancelled();
            }

            @Override
            public int order() {
                return annotation.order();
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return RequestMapping.class;
            }

        };
    }
}
