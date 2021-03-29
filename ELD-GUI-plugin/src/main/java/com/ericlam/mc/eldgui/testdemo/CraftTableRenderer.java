package com.ericlam.mc.eldgui.testdemo;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.*;
import com.ericlam.mc.eldgui.exceptions.RendererNotFoundException;
import com.ericlam.mc.eldgui.exceptions.TemplateNotFoundException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;
import java.util.List;
import java.util.Random;

public class CraftTableRenderer implements UIRenderer {


    private boolean is9Diamond(List<ItemStack> itemStacks){
        return itemStacks.stream().allMatch(i -> i.getType() == Material.DIAMOND);
    }

    private boolean is9Iron(List<ItemStack> itemStacks){
        return itemStacks.stream().allMatch(i -> i.getType() == Material.IRON_INGOT);
    }


    @Inject
    private ItemStackService itemStackService;

    @Inject
    private InventoryFactoryService factoryService;

    @Override
    public void render(InventoryScope attributes, UIOperation operation, Player player) {

        operation.addClickEvent('B', ClickCondition.name("craft"), e -> {
            var items = operation.getItems('A');
            items.forEach(i -> player.sendMessage(i.getType().toString())); //
            if (is9Diamond(items)){
                player.sendMessage("is diamond"); //
                operation.addItem('X', new ItemStack(Material.DIAMOND_BLOCK));
                operation.fillItem('A', new ItemStack(Material.AIR));
            }else if (is9Iron(items)){
                player.sendMessage("is iron"); //
                operation.addItem('X', new ItemStack(Material.IRON_BLOCK));
                operation.fillItem('A', new ItemStack(Material.AIR));
            }else{
                player.sendMessage("nothing crafted");
            }
        });

        operation.addClickEvent('X', ClickCondition.name("cancel-place").setActions(List.of(
                InventoryAction.PLACE_ONE,
                InventoryAction.PLACE_ALL,
                InventoryAction.PLACE_SOME
        )), e -> e.setCancelled(true));


        operation.setItem('W', 0, itemStackService.build(Material.PAPER)
        .display("數值")
        .lore(List.of(
                "&6[傳遞數值]: "+attributes.getSessionScope().getAttribute("pass", -1),
                "&b上一個背包: "+attributes.getSessionScope().getAttribute("last", "NONE")
        )).getItem());

        operation.addClickEvent('M', ClickCondition.clickType(List.of(ClickType.LEFT, ClickType.RIGHT)), e -> {
            e.setCancelled(true);
            try {
                operation.redirect(factoryService.getDispatcher("apple-shop"));
            } catch (TemplateNotFoundException | RendererNotFoundException ex) {
                e.getWhoClicked().sendMessage("redirect failed.");
            }
        });
    }

    @Override
    public void onCreate(InventoryScope scope, Player player) {
        scope.getSessionScope().setAttributeIfAbsent("pass", new Random().nextInt(100));
        player.sendMessage("crafttable, on create");
    }

    @Override
    public void onDestroy(InventoryScope scope, UIOperation operation, Player player) {
        player.sendMessage("crafttable, on destroy");
        scope.getSessionScope().setAttribute("last", "crafttable");
        operation.getItems('A').forEach(item -> player.getInventory().addItem(item));
    }
}
