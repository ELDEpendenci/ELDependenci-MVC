package com.ericlam.mc.eldgui.testdemo;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.InventoryFactoryService;
import com.ericlam.mc.eldgui.InventoryScope;
import com.ericlam.mc.eldgui.UIAction;
import com.ericlam.mc.eldgui.UIRenderer;
import com.ericlam.mc.eldgui.event.UIClickEvent;
import com.ericlam.mc.eldgui.event.UIHandler;
import com.ericlam.mc.eldgui.exception.RendererNotFoundException;
import com.ericlam.mc.eldgui.exception.TemplateNotFoundException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
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


    @UIHandler(patterns = {'A'}, filterClicks = {ClickType.LEFT, ClickType.RIGHT})
    public void onAClick(final UIClickEvent e) {
        if (e.getOriginEvent().getClick() == ClickType.LEFT) {
            handleBuy(e, 5, 1);
        } else {
            handleSell(e, 1, 5);
        }
    }

    @UIHandler(patterns = {'B'}, filterClicks = {ClickType.LEFT, ClickType.RIGHT})
    public void onBClick(final UIClickEvent e) {
        if (e.getOriginEvent().getClick() == ClickType.LEFT) {
            handleBuy(e, 15, 3);
        } else {
            handleSell(e, 3, 15);
        }
    }

    @UIHandler(patterns = {'C'}, filterClicks = {ClickType.LEFT, ClickType.RIGHT})
    public void onCClick(final UIClickEvent e) {
        if (e.getOriginEvent().getClick() == ClickType.LEFT) {
            handleBuy(e, 25, 5);
        } else {
            handleSell(e, 5, 25);
        }
    }

    @UIHandler(patterns = {'D'}, filterClicks = {ClickType.LEFT, ClickType.RIGHT})
    public void onDClick(final UIClickEvent e) {
        if (e.getOriginEvent().getClick() == ClickType.LEFT) {
            handleBuy(e, 35, 7);
        } else {
            handleSell(e, 7, 35);
        }
    }

    @UIHandler(patterns = {'E'}, filterClicks = {ClickType.LEFT, ClickType.RIGHT})
    public void onEClick(final UIClickEvent e) {
        if (e.getOriginEvent().getClick() == ClickType.LEFT) {
            handleBuy(e, 50, 10);
        } else {
            handleSell(e, 10, 50);
        }
    }

    @Override
    public void render(InventoryScope attributes, UIAction operation, Player player) {

        operation.setItem('Z', 0, itemStackService.build(Material.PAPER)
                .display("&a你的交易記錄")
                .lore(List.of(
                        "&e - 總購買數: " + totalBought.getOrDefault(player.getUniqueId(), 0),
                        "&e - 總售賣數: " + totalSold.getOrDefault(player.getUniqueId(), 0),
                        "&6 [傳遞數值]: " + attributes.getSessionScope().getAttribute("pass", -1),
                        "&b [上一個背包名稱]: " + attributes.getSessionScope().getAttribute("last", "NONE")
                )).getItem());

        try {
            operation.setDirectItem('X', factoryService.getDispatcher("crafttable"));
        } catch (TemplateNotFoundException | RendererNotFoundException e) {
           throw new IllegalStateException("cannot find crafttable inventory", e);
        }

    }

    @Override
    public void onCreate(InventoryScope scope, Player player) {
        scope.getSessionScope().setAttributeIfAbsent("pass", new Random().nextInt(100));
        player.sendMessage("apple-shop gui, on create");
    }

    @Override
    public void onDestroy(InventoryScope scope, UIAction operation, Player player) {
        player.sendMessage("apple-shop, on destroy");
        scope.getSessionScope().setAttribute("last", "apple-shop");
    }

    private void handleBuy(UIClickEvent e, int take, int give) {
        tradeItem(e, take, give, Material.STONE, Material.APPLE, totalBought);
    }

    private void tradeItem(UIClickEvent ex, int take, int give, Material takeMaterial, Material giveMaterial, Map<UUID, Integer> totalMap) {
        var e = ex.getOriginEvent();
        e.setCancelled(true);
        if (playerTakeItem(e.getWhoClicked().getInventory(), takeMaterial, take)) {
            e.getWhoClicked().sendMessage("successfully traded");
            playerGiveItem(e.getWhoClicked().getInventory(), giveMaterial, give);
            var uuid = e.getWhoClicked().getUniqueId();
            totalMap.put(uuid, totalMap.getOrDefault(uuid, 0) + 1);
            ex.getUIAction().rerender();
        } else {
            e.getWhoClicked().sendMessage("trade failed");
        }
    }

    private void handleSell(UIClickEvent e, int take, int give) {
        tradeItem(e, take, give, Material.APPLE, Material.STONE, totalSold);
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
            if (content.getAmount() > a) {
                content.setAmount(content.getAmount() - a);
                return true;
            } else {
                inv.remove(content);
                a -= content.getAmount();
                if (a == 0) return true;
            }
        }
        inv.setContents(beforeContent);
        return false;
    }

    private void playerGiveItem(PlayerInventory inv, Material m, int amount) {
        while (amount > 64) {
            playerGiveItem(inv, m, amount -= 64);
        }
        var items = new ItemStack(m, amount);
        inv.addItem(items);
    }

}
