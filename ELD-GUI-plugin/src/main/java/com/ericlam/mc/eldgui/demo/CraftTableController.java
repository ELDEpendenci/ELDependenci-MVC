package com.ericlam.mc.eldgui.demo;

import com.ericlam.mc.eldgui.LiveData;
import com.ericlam.mc.eldgui.controller.ControllerForView;
import com.ericlam.mc.eldgui.controller.FromPattern;
import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.event.BaseHandler;
import com.ericlam.mc.eldgui.event.ClickHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@ControllerForView(CraftTableView.class)
public class CraftTableController implements UIController {

    @ClickHandler(base = @BaseHandler(patterns = {'B'}))
    public void onClickCraft(@FromPattern('A') List<ItemStack> items, LiveData<CraftTableModel> data, Player player) {
        player.sendMessage("crafting....");
        data.update(model -> {
            if (is9Diamond(items)) {
                model.setCraftResult(new ItemStack(Material.DIAMOND_BLOCK));
                model.setChanged(true);
                player.sendMessage("crafted diamond block!");
            } else if (is9Iron(items)) {
                model.setCraftResult(new ItemStack(Material.IRON_BLOCK));
                model.setChanged(true);
                player.sendMessage("crafted iron block!");
            }
        });
    }

    @ClickHandler(base = @BaseHandler(patterns = {'M'}))
    public String goToPreviousGUI() {
        return "apple-shop";
    }


    private boolean is9Diamond(List<ItemStack> itemStacks) {
        return itemStacks.stream().allMatch(i -> i.getType() == Material.DIAMOND);
    }

    private boolean is9Iron(List<ItemStack> itemStacks) {
        return itemStacks.stream().allMatch(i -> i.getType() == Material.IRON_INGOT);
    }

}
