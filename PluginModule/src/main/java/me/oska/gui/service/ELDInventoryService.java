package me.oska.gui.service;

import me.oska.gui.InventoryUI;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ELDInventoryService implements InventoryService {

    @Inject
    private Map<String, InventoryUI> serviceRenderers;
    private Map<String, Class<? extends InventoryUI>> renderers;
    public Map<String, Class<? extends InventoryUI>> getRenderers() {
        return this.renderers;
    }

    private Map<String, Stack<ELDInventoryState>> activeInventories;
    public InventoryState getActiveInventory(String uuid) {
        Stack<ELDInventoryState> state = activeInventories.get(uuid);
        if (state == null) {
            return null;
        }
        return state.peek();
    }

    public ELDInventoryService() {
        this.activeInventories = new HashMap<>();
        this.renderers = new HashMap<>();
    }

    @Override
    public InventoryNavigator holder(HumanEntity player) {
        return new ELDInventoryNavigator(player);
    }

    @Override
    public void register(String route, Class<? extends InventoryUI<?>> renderer) {
        renderers.put(route, renderer);
    }

    public class ELDInventoryNavigator implements InventoryNavigator {

        private HumanEntity player;

        public ELDInventoryNavigator(HumanEntity player) {
            this.player = player;
        }

        @Override
        public void navigate(String route, Object state) {
            String uuid = player.getUniqueId().toString();
            activeInventories.compute(uuid, (__, lists) -> {
                if (lists == null) {
                    lists = new Stack<>();
                }

                InventoryUI renderer = serviceRenderers.get(route);
                Inventory inventory = renderer.createInventory(state);
                renderer.render(inventory, state);

                lists.push(ELDInventoryState.buildState(route, state, inventory, renderer));

                player.closeInventory();
                player.openInventory(inventory);
                return lists;
            });

        }

        @Override
        public void replace(String route, Object state) {
            String uuid = player.getUniqueId().toString();
            Stack<ELDInventoryState> lists = new Stack<>();

            InventoryUI renderer = serviceRenderers.get(route);
            Inventory inventory = renderer.createInventory(state);
            renderer.render(inventory,state);

            lists.push(ELDInventoryState.buildState(route, state, inventory, renderer));
            activeInventories.replace(uuid, lists);

            player.closeInventory();
            player.openInventory(inventory);
        }

        @Override
        public void reload(Object state) {
            String uuid = player.getUniqueId().toString();
            Stack<ELDInventoryState> lists = activeInventories.get(uuid);
            ELDInventoryState last = lists.peek();
            ELDInventoryState newState = ELDInventoryState.buildState(last.getRoute(), state, last.getInventory(), last.getRenderer());
            newState.getRenderer().render(newState.getInventory(), newState.getState());

            lists.set(0, newState);
        }

        @Override
        public void goBack() {
            String uuid = player.getUniqueId().toString();
            activeInventories.compute(uuid, (__, lists) -> {
                ELDInventoryState last = lists.pop();
                if (lists.size() == 0) {
                    player.closeInventory();
                    return null;
                }

                last.getRenderer().render(last.getInventory(), last.getState());

                player.closeInventory(InventoryCloseEvent.Reason.OPEN_NEW);
                player.openInventory(last.getInventory());
                return lists;
            });
        }

        @Override
        public void goBack(int num) {
            if (num < 1) {
                throw new IllegalArgumentException("Argument `num` must be bigger than zero.");
            }

            String uuid = player.getUniqueId().toString();
            activeInventories.compute(uuid, (__, lists) -> {
                for (int i = 0; i < num; i++) {
                    ELDInventoryState last = lists.pop();
                    if (lists.size() == 0) {
                        player.closeInventory();
                        return null;
                    }
                }

                ELDInventoryState last = lists.peek();
                last.getRenderer().render(last.getInventory(), last.getState());

                player.closeInventory(InventoryCloseEvent.Reason.OPEN_NEW);
                player.openInventory(last.getInventory());
                return lists;
            });
        }
    }
}
