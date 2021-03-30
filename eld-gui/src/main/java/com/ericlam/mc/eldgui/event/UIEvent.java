package com.ericlam.mc.eldgui.event;

import com.ericlam.mc.eldgui.InventoryScope;
import com.ericlam.mc.eldgui.UIAction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryEvent;

public abstract class UIEvent<E extends InventoryEvent> {

    private final Player owner;
    private final UIAction uiAction;
    private final InventoryScope scope;
    private final E originEvent;

    public UIEvent(Player owner, UIAction uiAction, InventoryScope scope, E originEvent) {
        this.owner = owner;
        this.uiAction = uiAction;
        this.scope = scope;
        this.originEvent = originEvent;
    }

    public Player getOwner() {
        return owner;
    }

    public UIAction getUIAction() {
        return uiAction;
    }

    public InventoryScope getScope() {
        return scope;
    }

    public E getOriginEvent() {
        return originEvent;
    }
}
