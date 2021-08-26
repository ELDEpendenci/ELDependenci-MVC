package com.ericlam.mc.eldgui.component;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.component.modifier.Listenable;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public final class TextInputField extends AbstractComponent implements Listenable<AsyncChatEvent> {

    private final boolean disabled;
    private final long maxWait;
    private final String inputMessage;

    public TextInputField(
            AttributeController attributeController,
            ItemStackService.ItemFactory itemFactory,
            boolean disabled,
            long maxWait,
            String inputMessage
    ) {
        super(attributeController, itemFactory);
        this.disabled = disabled;
        this.maxWait = maxWait;
        this.inputMessage = inputMessage;
        itemFactory.lore("-> " + attributeController.getAttribute(getItem(), AttributeController.VALUE_TAG));
        if (disabled) itemFactory.lore("&cDisabled");
    }

    @Override
    public void onListen(Player player) {
        player.sendMessage(inputMessage);
    }

    @Override
    public long getMaxWaitingTime() {
        return maxWait;
    }

    @Override
    public void callBack(AsyncChatEvent event) {
        final String message = ((TextComponent) event.message()).content();
        attributeController.setAttribute(getItem(), AttributeController.VALUE_TAG, message);
        itemFactory.lore(List.of("-> " + message));
        this.updateInventory();
    }

    @Override
    public Class<AsyncChatEvent> getEventClass() {
        return AsyncChatEvent.class;
    }

    @Override
    public boolean shouldActivate(InventoryClickEvent e) {
        return !disabled;
    }
}
