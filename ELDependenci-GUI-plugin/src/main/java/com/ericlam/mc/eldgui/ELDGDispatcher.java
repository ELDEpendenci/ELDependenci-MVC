package com.ericlam.mc.eldgui;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ELDGDispatcher implements UIDispatcher {

    private static final Map<Player, UISession> uiSessionMap = new ConcurrentHashMap<>();


    private final Object controller;
    private final ViewJumper goTo;
    private final Map<Player, ELDGUI> guiSessionMap = new ConcurrentHashMap<>();

    @Inject
    private Injector injector;
    @Inject
    private ManagerFactory managerFactory;
    @Inject
    private ELDGMVCInstallation eldgmvcInstallation;

    public ELDGDispatcher(
            Object controller,
            ViewJumper goTo
    ) {
        this.controller = controller;
        this.goTo = (session, player, ui) -> {
            uiSessionMap.put(player, session);
            goTo.onJump(session, player, ui);
        };
    }


    @Override
    public void openFor(Player player) {
        UISession session = Optional.ofNullable(uiSessionMap.remove(player)).orElseGet(ELDGUISession::new);

        ELDGUI eldgui = new ELDGUI(
                controller,
                session,
                player,
                managerFactory,
                guiSessionMap::remove,
                goTo,
                eldgmvcInstallation
        );
        injector.injectMembers(eldgui);
        this.guiSessionMap.put(player, eldgui);
    }

    @Override
    public void openForGlobal(Player player) {
    }

    public synchronized void onClose() {
        this.guiSessionMap.values().forEach(ELDGUI::destroy);
    }

    @SuppressWarnings("unchecked")
    private final static class ELDGUISession implements UISession {

        private final Map<String, Object> attributes = new ConcurrentHashMap<>();


        @Override
        public <T> T getAttribute(String key) {
            return (T) attributes.get(key);
        }

        @Override
        public void setAttribute(String key, Object item) {
            this.attributes.put(key, item);
        }

        @Override
        public <T> T pollAttribute(String key) {
            return (T)this.attributes.remove(key);
        }
    }
}
