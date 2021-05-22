package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.model.Model;
import com.ericlam.mc.eldgui.view.View;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ELDGDispatcher<E extends Model> implements UIDispatcher {

    private static final Map<Player, UISession> uiSessionMap = new ConcurrentHashMap<>();


    private final View<E> view;
    private final UIController controller;
    private final MethodParseFactory methodParseFactory;
    private final ItemStackService itemStackService;
    private final InventoryTemplate inventoryTemplate;
    private final ViewJumper goTo;
    private final Map<Player, ELDGUI<E>> guiSessionMap = new ConcurrentHashMap<>();


    public ELDGDispatcher(
            View<E> view,
            UIController controller,
            MethodParseFactory factory,
            ItemStackService itemStackService,
            InventoryTemplate inventoryTemplate,
            ViewJumper goTo
    ) {
        this.view = view;
        this.controller = controller;
        this.methodParseFactory = factory;
        this.inventoryTemplate = inventoryTemplate;
        this.itemStackService = itemStackService;
        this.goTo = (session, player, ui) -> {
            uiSessionMap.put(player, session);
            goTo.onJump(session, player, ui);
        };
    }


    @Override
    public void openFor(Player player) {
        if (guiSessionMap.containsKey(player)) {
            ELDGUI<E> eldgui = guiSessionMap.get(player);
            eldgui.resume();
            player.openInventory(eldgui.getNativeInventory());
            return;
        }
        UISession session = Optional.ofNullable(uiSessionMap.remove(player)).orElseGet(ELDGUISession::new);
        ELDGUI<E> eldgui = new ELDGUI<>(
                inventoryTemplate,
                view,
                controller,
                itemStackService,
                session,
                player,
                methodParseFactory,
                guiSessionMap::remove,
                goTo
        );
        this.guiSessionMap.put(player, eldgui);
        player.openInventory(eldgui.getNativeInventory());
    }

    @Override
    public void openForGlobal(Player player) {
        this.guiSessionMap.values().stream().findFirst().ifPresentOrElse(
                g -> player.openInventory(g.getNativeInventory()),
                () -> openFor(player)
        );
    }

    private final static class ELDGUISession implements UISession {

        private final Map<String, Object> attributes = new ConcurrentHashMap<>();

        @SuppressWarnings("unchecked")
        @Override
        public <T> T getAttribute(String key) {
            return (T) attributes.get(key);
        }

        @Override
        public void setAttribute(String key, Object item) {
            this.attributes.put(key, item);
        }

        @Override
        public boolean removeAttribute(String key) {
            return this.attributes.remove(key) != null;
        }
    }


    public synchronized void onClose(){
        this.guiSessionMap.values().forEach(ELDGUI::destroy);
    }
}
