package com.ericlam.mc.eldgui.testdemo;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.*;
import com.ericlam.mc.eldgui.exceptions.RendererNotFoundException;
import com.ericlam.mc.eldgui.exceptions.TemplateNotFoundException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AppleShopRenderer implements UIRenderer {

    @Inject
    private ItemStackService itemStackService;

    @Inject
    private InventoryFactoryService factoryService;

    private final Map<UUID, Integer> totalBought = new ConcurrentHashMap<>();

    private final Map<UUID, Integer> totalSold = new ConcurrentHashMap<>();

    private static class State {
        private char renderer = '\0';
    }

    @Override
    public void render(InventoryScope attributes, UIOperation operation, Player player) {
        attributes.setAttributeIfAbsent("renderer", new State());
        var state = attributes.getAttribute("renderer", State.class);
        if (state.renderer != 'Z') {
            Runnable callback = () -> {
                attributes.getAttribute("renderer", State.class).renderer = 'Z';
                operation.rerender();
            };
            operation.addClickEvent('A',
                    ClickCondition
                            .name("take-stone-5-1")
                            .setClickType(List.of(ClickType.LEFT)),
                    e -> handleBuy(e, 5, 1, callback));
            operation.addClickEvent('A', ClickCondition
                            .name("give-stone-5-1")
                            .setClickType(List.of(ClickType.RIGHT)),
                    e -> handleSell(e, 1, 5, callback));

            operation.addClickEvent('B',
                    ClickCondition
                            .name("take-stone-3-15")
                            .setClickType(List.of(ClickType.LEFT)),
                    e -> handleBuy(e, 15, 3, callback));
            operation.addClickEvent('B', ClickCondition
                            .name("give-stone-3-15")
                            .setClickType(List.of(ClickType.RIGHT)),
                    e -> handleSell(e, 3, 15, callback));

            operation.addClickEvent('C',
                    ClickCondition
                            .name("take-stone-5-25")
                            .setClickType(List.of(ClickType.LEFT)),
                    e -> handleBuy(e, 25, 5, callback));
            operation.addClickEvent('C', ClickCondition
                            .name("give-stone-5-25")
                            .setClickType(List.of(ClickType.RIGHT)),
                    e -> handleSell(e, 5, 25, callback));

            operation.addClickEvent('D',
                    ClickCondition
                            .name("take-stone-7-35")
                            .setClickType(List.of(ClickType.LEFT)),
                    e -> handleBuy(e, 35, 7, callback));
            operation.addClickEvent('D', ClickCondition
                            .name("give-stone-7-35")
                            .setClickType(List.of(ClickType.RIGHT)),
                    e -> handleSell(e, 7, 35, callback));

            operation.addClickEvent('E',
                    ClickCondition
                            .name("take-stone-10-50")
                            .setClickType(List.of(ClickType.LEFT)),
                    e -> handleBuy(e, 50, 10, callback));
            operation.addClickEvent('E', ClickCondition
                            .name("give-stone-10-50")
                            .setClickType(List.of(ClickType.RIGHT)),
                    e -> handleSell(e, 10, 50, callback));
        }

        operation.setItem('Z', 0, itemStackService.build(Material.PAPER)
                .display("&a你的交易記錄")
                .lore(List.of(
                        "&e - 總購買數: " + totalBought.getOrDefault(player.getUniqueId(), 0),
                        "&e - 總售賣數: " + totalSold.getOrDefault(player.getUniqueId(), 0),
                        "&6 [傳遞數值]: "+attributes.getSessionScope().getAttribute("pass", -1),
                        "&b [上一個背包名稱]: "+attributes.getSessionScope().getAttribute("last", "NONE")
                )).getItem());

        operation.addClickEvent('X', ClickCondition.clickType(List.of(ClickType.LEFT, ClickType.RIGHT)), e -> {
            e.setCancelled(true);
            try {
                operation.redirect(factoryService.getDispatcher("crafttable"));
            } catch (TemplateNotFoundException | RendererNotFoundException ex) {
                e.getWhoClicked().sendMessage("redirect failed.");
            }
        });
    }

    @Override
    public void onCreate(InventoryScope scope, Player player) {
        scope.getSessionScope().setAttributeIfAbsent("pass", new Random().nextInt(100));
        player.sendMessage("apple-shop gui, on create");
    }

    @Override
    public void onDestroy(InventoryScope scope, UIOperation operation, Player player) {
        player.sendMessage("apple-shop, on destroy");
        scope.getSessionScope().setAttribute("last", "apple-shop");
    }

    private void handleBuy(InventoryClickEvent e, int take, int give, Runnable call) {
        tradeItem(e, take, give, Material.STONE, Material.APPLE, totalBought, call);
    }

    private void tradeItem(InventoryClickEvent e, int take, int give, Material takeMaterial, Material giveMaterial, Map<UUID, Integer> totalMap, Runnable callback) {
        e.setCancelled(true);
        if (playerTakeItem(e.getWhoClicked().getInventory(), takeMaterial, take)) {
            e.getWhoClicked().sendMessage("successfully traded");
            playerGiveItem(e.getWhoClicked().getInventory(), giveMaterial, give);
            var uuid = e.getWhoClicked().getUniqueId();
            totalMap.put(uuid, totalMap.getOrDefault(uuid, 0) + 1);
            callback.run();
        } else {
            e.getWhoClicked().sendMessage("trade failed");
        }
    }

    private void handleSell(InventoryClickEvent e, int take, int give, Runnable call) {
        tradeItem(e, take, give, Material.APPLE, Material.STONE, totalSold, call);
    }

    private boolean playerHasItem(PlayerInventory inv, Material m, int amount) {
        int a = 0;
        for (ItemStack content : inv.getContents()) {
            if (content.getType() != m) continue;
            a += content.getAmount();
        }
        return a > amount;
    }

    private boolean playerTakeItem(PlayerInventory inv, Material m, int amount) {
        int a = amount;
        var beforeContent = inv.getContents().clone();
        for (ItemStack content : inv.getContents()) {
            if (content == null || content.getType() != m) continue;
            if (content.getAmount() > a){
                content.setAmount(content.getAmount() - a);
                return true;
            }else{
                inv.remove(content);
                a -= content.getAmount();
                if (a == 0) return true;
            }
        }
        inv.setContents(beforeContent);
        return false;
    }

    private void playerGiveItem(PlayerInventory inv, Material m, int amount){
        while (amount > 64){
            playerGiveItem(inv, m, amount -= 64);
        }
        var items = new ItemStack(m, amount);
        inv.addItem(items);
    }

}
