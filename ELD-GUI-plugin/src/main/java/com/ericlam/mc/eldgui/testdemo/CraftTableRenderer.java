package com.ericlam.mc.eldgui.testdemo;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.*;
import com.ericlam.mc.eldgui.event.UIClickEvent;
import com.ericlam.mc.eldgui.event.UIHandler;
import com.ericlam.mc.eldgui.exception.RendererNotFoundException;
import com.ericlam.mc.eldgui.exception.TemplateNotFoundException;
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


    @UIHandler(patterns = {'B'})
    public void onCraft(UIClickEvent e){
        var items = e.getUIAction().getItems('A');
        items.forEach(i -> e.getOwner().sendMessage(i.getType().toString())); //
        if (is9Diamond(items)){
            e.getOwner().sendMessage("is diamond"); //
            e.getUIAction().addItem('X', new ItemStack(Material.DIAMOND_BLOCK));
            e.getUIAction().fillItem('A', new ItemStack(Material.AIR));
        }else if (is9Iron(items)){
            e.getOwner().sendMessage("is iron"); //
            e.getUIAction().addItem('X', new ItemStack(Material.IRON_BLOCK));
            e.getUIAction().fillItem('A', new ItemStack(Material.AIR));
        }else{
            e.getOwner().sendMessage("nothing crafted");
        }
    }

    @UIHandler(patterns = 'X', filterActions = {InventoryAction.PLACE_ONE, InventoryAction.PLACE_ALL, InventoryAction.PLACE_SOME})
    public void onCancel(UIClickEvent e){
        e.getOriginEvent().setCancelled(true);
    }

    @Override
    public void render(InventoryScope attributes, UIAction operation, Player player) {

        operation.setItem('W', 0, itemStackService.build(Material.PAPER)
        .display("數值")
        .lore(List.of(
                "&6[傳遞數值]: "+attributes.getSessionScope().getAttribute("pass", -1),
                "&b上一個背包: "+attributes.getSessionScope().getAttribute("last", "NONE")
        )).getItem());

        try {
            operation.setDirectItem('M', factoryService.getDispatcher("apple-shop"));
        } catch (TemplateNotFoundException | RendererNotFoundException ex) {
            throw new IllegalStateException("unknown shop apple-shop", ex);
        }
    }

    @Override
    public void onCreate(InventoryScope scope, Player player) {
        scope.getSessionScope().setAttributeIfAbsent("pass", new Random().nextInt(100));
        player.sendMessage("crafttable, on create");
    }

    @Override
    public void onDestroy(InventoryScope scope, UIAction operation, Player player) {
        player.sendMessage("crafttable, on destroy");
        scope.getSessionScope().setAttribute("last", "crafttable");
        operation.getItems('A').forEach(item -> player.getInventory().addItem(item));
    }
}
