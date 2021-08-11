package com.ericlam.mc.eldgui.event;

import com.ericlam.mc.eldgui.ELDGView;
import com.ericlam.mc.eldgui.MVCInstallation;
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
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public final class ELDGClickEventHandler extends ELDGEventHandler<ClickMapping, InventoryClickEvent> {


    public ELDGClickEventHandler(Object controller, MethodParseManager parseManager, ReturnTypeManager returnTypeManager, Map<Class<? extends Annotation>, MVCInstallation.QualifierFilter<? extends Annotation>> customQualifier) {
        super(controller, parseManager, returnTypeManager, customQualifier);
    }

    @Override
    protected Map<ClickMapping, Method> loadAllHandlers(Object controller) {
        return  Arrays.stream(controller.getClass().getDeclaredMethods()).parallel()
                .filter(m -> m.isAnnotationPresent(ClickMapping.class))
                .collect(Collectors.toMap(m -> m.getAnnotation(ClickMapping.class), m -> m));
    }


    @Override
    public void onEventHandle(InventoryClickEvent e, Player player, ELDGView<?> eldgView) throws Exception{
        if (eldgView.getNativeInventory() != e.getClickedInventory()) return;
        if (e.getSlotType() != InventoryType.SlotType.CONTAINER) return;
        super.onEventHandle(e, player, eldgView);
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
            public Class<? extends Annotation> annotationType() {
                return RequestMapping.class;
            }

        };
    }
}
