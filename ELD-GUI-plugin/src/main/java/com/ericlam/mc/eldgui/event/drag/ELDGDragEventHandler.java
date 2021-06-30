package com.ericlam.mc.eldgui.event.drag;

import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.event.DragHandler;
import com.ericlam.mc.eldgui.event.ELDGEventHandler;
import com.ericlam.mc.eldgui.event.MethodParseManager;
import com.ericlam.mc.eldgui.event.ReturnTypeManager;
import org.bukkit.event.inventory.InventoryDragEvent;

import java.util.Arrays;
import java.util.List;

public final class ELDGDragEventHandler extends ELDGEventHandler<DragHandler, InventoryDragEvent> {

    public ELDGDragEventHandler(UIController controller, MethodParseManager parseManager, ReturnTypeManager returnTypeManager) {
        super(controller, parseManager, returnTypeManager);
    }

    @Override
    protected void loadAllHandlers(UIController controller) {
        Arrays.stream(controller.getClass().getDeclaredMethods())
                .parallel()
                .filter(m -> m.isAnnotationPresent(DragHandler.class))
                .forEach(m -> eventMap.put(m.getAnnotation(DragHandler.class), m));
    }

    @Override
    protected boolean slotTrigger(List<Integer> slots, InventoryDragEvent event) {
        return slots.stream().anyMatch(s -> event.getInventorySlots().contains(s));
    }


    @Override
    protected boolean annotationFilter(DragHandler annotate, InventoryDragEvent event) {
        boolean dragMatch = annotate.dragTypes().length == 0 || Arrays.binarySearch(annotate.dragTypes(), event.getType()) >= 0;
        boolean itemsMatch = annotate.dragItems().length == 0 ||  Arrays.binarySearch(annotate.dragItems(), event.getOldCursor().getType()) >= 0;
        return dragMatch && itemsMatch;
    }
}
