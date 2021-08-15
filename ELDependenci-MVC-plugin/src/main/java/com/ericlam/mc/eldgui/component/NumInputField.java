package com.ericlam.mc.eldgui.component;

import com.ericlam.mc.eld.services.ItemStackService;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class NumInputField extends AbstractComponent implements ClickableComponent, ListenableComponent<AsyncChatEvent> {

    private final int min, max;
    private final boolean disabled;
    private final String inputMessage;
    private final String errorMessage;
    private final long maxWait;

    private int value;

    public NumInputField(
            AttributeController attributeController,
            ItemStackService.ItemFactory itemFactory,
            int min,
            int max,
            boolean disabled,
            String inputMessage,
            String errorMessage,
            long maxWait
    ) {
        super(attributeController, itemFactory);
        this.min = min;
        this.max = max;
        this.disabled = disabled;
        this.inputMessage = inputMessage;
        this.errorMessage = errorMessage;
        this.maxWait = maxWait;
        this.value = Optional.ofNullable((Integer) attributeController.getAttribute(getItem(), AttributeController.VALUE_TAG)).orElse(0);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        int value;
        if (event.isLeftClick()) {
            value = Math.min(max, this.value + 1);
        } else if (event.isRightClick()) {
            value = Math.max(min, this.value - 1);
        } else {
            return;
        }
        this.updateValue(value);
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public void onListen(Player player) {
        Map<String, Object> properties = Map.of("min", min, "max", max);
        player.sendMessage(StrSubstitutor.replace(inputMessage, properties));
    }

    @Override
    public long getMaxWaitingTime() {
        return maxWait;
    }

    @Override
    public void callBack(AsyncChatEvent event) {
        String msg = ((TextComponent) event.message()).content();
        try {
            int value = Integer.parseInt(msg);
            if (value < min || value > max) throw new NumberFormatException();
            this.updateValue(value);
        } catch (NumberFormatException e) {
            event.getPlayer().sendMessage(errorMessage);
        }
    }

    @Override
    public Class<AsyncChatEvent> getEventClass() {
        return AsyncChatEvent.class;
    }

    @Override
    public boolean shouldActivate(InventoryClickEvent e) {
        return e.getClick() == ClickType.MIDDLE;
    }


    private void updateValue(int value) {
        this.value = value;
        attributeController.setAttribute(getItem(), AttributeController.VALUE_TAG, this.value);
        itemFactory.lore(List.of("-> " + this.value));
        this.updateInventory();
    }
}
