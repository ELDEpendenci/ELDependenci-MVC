package com.ericlam.mc.eldgui.event.drag;

import com.ericlam.mc.eldgui.event.*;
import com.ericlam.mc.eldgui.view.View;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ELDGDragEventHandler extends ELDGEventHandler<DragMapping, InventoryDragEvent> {

    public ELDGDragEventHandler(Object controller, MethodParseManager parseManager, ReturnTypeManager returnTypeManager) {
        super(controller, parseManager, returnTypeManager);
    }

    @Override
    protected Map<DragMapping, Method> loadAllHandlers(Object controller) {
        return Arrays.stream(controller.getClass().getDeclaredMethods())
                .parallel()
                .filter(m -> m.isAnnotationPresent(DragMapping.class))
                .collect(Collectors.toMap(m -> m.getAnnotation(DragMapping.class), m -> m));
    }


    @Override
    protected boolean slotTrigger(List<Integer> slots, InventoryDragEvent event) {
        return slots.stream().anyMatch(s -> event.getInventorySlots().contains(s));
    }

    @Override
    protected RequestMapping toRequestMapping(DragMapping annotation) {
        return new RequestMapping() {

            @Override
            public Class<? extends View<?>> view() {
                return annotation.view();
            }

            @Override
            public Class<? extends InventoryInteractEvent> event() {
                return InventoryDragEvent.class;
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
