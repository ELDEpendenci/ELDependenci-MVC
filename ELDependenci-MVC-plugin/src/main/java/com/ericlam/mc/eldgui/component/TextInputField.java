package com.ericlam.mc.eldgui.component;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.component.factory.AttributeController;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;

import java.util.List;

public final class TextInputField extends AbstractComponent implements ListenableComponent<AsyncChatEvent> {

    public TextInputField(AttributeController attributeController, ItemStackService.ItemFactory itemFactory) {
        super(attributeController, itemFactory);
    }

    @Override
    public void onListen(Player player) {
        player.sendMessage("input the value within 10 seconds");
    }

    @Override
    public long getMaxWaitingTime() {
        return 200L; // 10 secs
    }

    @Override
    public void callBack(AsyncChatEvent event) {
        final String message = ((TextComponent) event.message()).content();
        attributeController.setAttribute(getItem(), AttributeController.VALUE_TAG, message);
        itemFactory.lore(List.of("Input: " + message));
    }

    @Override
    public Class<AsyncChatEvent> getEventClass() {
        return AsyncChatEvent.class;
    }
}
