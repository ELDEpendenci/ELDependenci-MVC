package com.ericlam.mc.eldgui.demo.crafttable;

import com.ericlam.mc.eldgui.UISession;
import com.ericlam.mc.eldgui.controller.ControllerForView;
import com.ericlam.mc.eldgui.controller.FromPattern;
import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.controller.UIRequest;
import com.ericlam.mc.eldgui.event.BaseHandler;
import com.ericlam.mc.eldgui.event.ClickHandler;
import com.ericlam.mc.eldgui.lifecycle.OnDestroy;
import com.ericlam.mc.eldgui.view.JumpToView;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@ControllerForView(CraftTableView.class)
public class CraftTableController implements UIController {

    @ClickHandler(base = @BaseHandler(patterns = {'B'}))
    public JumpToView onClickCraft(@FromPattern('A') List<ItemStack> items, UISession session, Player player) {
        if (is9Diamond(items)) {
            session.setAttribute("craft_result", Material.DIAMOND_BLOCK);
        } else if (is9Iron(items)) {
            session.setAttribute("craft_result", Material.IRON_BLOCK);
        }

        return new JumpToView("confirm", true);
    }

    @ClickHandler(base = @BaseHandler(patterns = {'M'}))
    public String goToPreviousGUI() {
        return "apple-shop";
    }


    @OnDestroy
    public void onDestroy(@FromPattern('A') List<ItemStack> items, Player player) {
        items.forEach(p -> player.getInventory().addItem(p));
    }

    private boolean is9Diamond(List<ItemStack> itemStacks) {
        return itemStacks.stream().allMatch(i -> i.getType() == Material.DIAMOND);
    }

    private boolean is9Iron(List<ItemStack> itemStacks) {
        return itemStacks.stream().allMatch(i -> i.getType() == Material.IRON_INGOT);
    }

}
