package me.oska.gui.service;


import org.bukkit.entity.Player;
import me.oska.gui.InventoryNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ELDInventoryService implements InventoryService {

    private static Map<String, Stack<InventoryNode>> inventories;

    public static Stack<InventoryNode> getByPlayer(String uuid) {
        return inventories.getOrDefault(uuid, null);
    }

    public ELDInventoryService() {
        this.inventories = new HashMap<>();
    }

    @Override
    public InventoryNavigator holder(Player player) {
        return new ELDInventoryNavigator(player);
    }

    public class ELDInventoryNavigator implements InventoryNavigator {

        private Player player;

        public ELDInventoryNavigator(Player player) {
            this.player = player;
        }

        @Override
        public void navigate(InventoryNode node) {
            String uuid = player.getUniqueId().toString();
            inventories.compute(uuid, (__, lists) -> {
                if (lists == null) {
                    lists = new Stack<>();
                }

                InventoryNode instance = node;
                if (!instance.options().isGlobalSharedInventory) {
                    instance = node.clone();
                }
//                injector.inject(instance);
                lists.push(instance);
                player.closeInventory();

                player.openInventory(instance.getInventory());
                instance.render();
                return lists;
            });

        }

        @Override
        public void replace(InventoryNode node) {
            String uuid = player.getUniqueId().toString();
            Stack<InventoryNode> lists = new Stack<>();
            InventoryNode instance = node;
            if (!instance.options().isGlobalSharedInventory) {
                instance = node.clone();
            }
//            injector.inject(instance);
            lists.push(instance);
            player.closeInventory();

            inventories.replace(uuid, lists);
            player.openInventory(instance.getInventory());
            instance.render();
        }

        @Override
        public void goBack() {
            String uuid = player.getUniqueId().toString();
            inventories.compute(uuid, (__, lists) -> {
                InventoryNode last = lists.pop();
                last.disposeState();
                player.closeInventory();
                if (lists.size() == 0) {
                    return null;
                }

                InventoryNode instance = lists.peek();
                if (instance.options().refreshStateWhenGoBack) {
                    instance.setState(instance.initialState());
                }

                player.openInventory(instance.getInventory());
                if (instance.options().renderWhenGoBack) {
                    instance.render();
                }
                return lists;
            });
        }

        @Override
        public void goBack(int num) {
            if (num < 1) {
                throw new IllegalArgumentException("Argument `num` must be bigger than zero.");
            }

            String uuid = player.getUniqueId().toString();
            inventories.compute(uuid, (__, lists) -> {
                for (int i = 0; i < num; i++) {
                    InventoryNode last = lists.pop();
                    last.disposeState();
                    if (lists.size() == 0) {
                        player.closeInventory();
                        return null;
                    }
                }
                player.closeInventory();

                InventoryNode instance = lists.peek();
                if (instance.options().refreshStateWhenGoBack) {
                    instance.setState(instance.initialState());
                }

                player.openInventory(instance.getInventory());
                if (instance.options().renderWhenGoBack) {
                    instance.render();
                }
                return lists;
            });
        }
    }
}
