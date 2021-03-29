package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.services.ItemStackService;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public final class ELDGDispatcher implements UIDispatcher {

    private final InventoryTemplate demoInventories;

    private final UIRenderer uiRenderer;

    private final ItemStackService itemStackService;

    public ELDGDispatcher(InventoryTemplate demoInventories, UIRenderer uiRenderer, ItemStackService itemStackService) {
        this.demoInventories = demoInventories;
        this.uiRenderer = uiRenderer;
        this.itemStackService = itemStackService;
    }

    @Override
    public void forward(Player player) {
        var scope = new GUIScope();
        scope.setSessionScope(new GUIScope());
        this.forward(player, scope);
    }

    public void forward(Player player, InventoryScope scope){
        var guiScope = new GUIScope();
        guiScope.setSessionScope(scope);
        ELDGUI eldgui = new ELDGUI(
                demoInventories,
                uiRenderer,
                itemStackService,
                guiScope,
                player);
        eldgui.render();
        player.openInventory(eldgui.getNativeInventory());
    }

    private static class GUIScope implements InventoryScope {

        private InventoryScope scope;
        private final Map<String, Object> attributes = new ConcurrentHashMap<>();

        @Override
        public void setAttributeIfAbsent(String key, Object value) {
            attributes.putIfAbsent(key, value);
        }

        @Override
        public void setAttribute(String key, Object value) {
            attributes.put(key, value);
        }

        @Override
        public Object getAttribute(String key) {
            return attributes.get(key);
        }

        @Override
        public <T> T getAttribute(String key, T defaultValue) {
            return (T) attributes.getOrDefault(key, defaultValue);
        }

        @Override
        public <T> T getAttribute(String key, Class<T> type) {
            return type.cast(attributes.get(key));
        }

        @Override
        public InventoryScope getSessionScope() {
            return Optional.ofNullable(scope).orElseThrow(() -> new UnsupportedOperationException("this is already session scope"));
        }

        public void setSessionScope(InventoryScope scope) {
            this.scope = scope;
        }
    }
}
