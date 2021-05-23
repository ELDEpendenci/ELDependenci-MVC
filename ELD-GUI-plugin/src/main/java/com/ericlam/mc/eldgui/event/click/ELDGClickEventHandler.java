package com.ericlam.mc.eldgui.event.click;

import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.event.ClickHandler;
import com.ericlam.mc.eldgui.event.ELDGEventHandler;
import com.ericlam.mc.eldgui.event.MethodParseManager;
import com.ericlam.mc.eldgui.event.ReturnTypeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class ELDGClickEventHandler extends ELDGEventHandler<ClickHandler, InventoryClickEvent> {


    public ELDGClickEventHandler(UIController controller, MethodParseManager parseManager, ReturnTypeManager returnTypeManager) {
        super(controller, parseManager, returnTypeManager);
    }

    protected void loadAllHandlers(UIController controller) {
        Arrays.stream(controller.getClass().getDeclaredMethods()).parallel()
                .filter(m -> m.isAnnotationPresent(ClickHandler.class))
                .forEach(m -> eventMap.put(m.getAnnotation(ClickHandler.class), m));
    }

    @Override
    public void onEventHandle(InventoryClickEvent e, Player player, Inventory nativeInventory, Map<Character, List<Integer>> patternMasks) {
        if (nativeInventory != e.getClickedInventory()) return;
        if (e.getSlotType() != InventoryType.SlotType.CONTAINER) return;
        super.onEventHandle(e, player, nativeInventory, patternMasks);
    }

    @Override
    protected int getSlotFromEvent(InventoryClickEvent event) {
        return event.getSlot();
    }

    @Override
    protected boolean annotationFilter(ClickHandler uiHandler, InventoryClickEvent e) {
        var actionEqual = uiHandler.actions().length == 0 || Arrays.binarySearch(uiHandler.actions(), e.getAction()) >= 0;
        var clickEqual = uiHandler.clicks().length == 0 || Arrays.binarySearch(uiHandler.clicks(), e.getClick()) >= 0;
        return actionEqual && clickEqual;
    }
}
