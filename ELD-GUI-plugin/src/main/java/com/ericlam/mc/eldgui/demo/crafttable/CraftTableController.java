package com.ericlam.mc.eldgui.demo.crafttable;

import com.ericlam.mc.eldgui.LiveData;
import com.ericlam.mc.eldgui.UISession;
import com.ericlam.mc.eldgui.controller.ControllerForView;
import com.ericlam.mc.eldgui.controller.FromPattern;
import com.ericlam.mc.eldgui.controller.UIController;
import com.ericlam.mc.eldgui.event.BaseHandler;
import com.ericlam.mc.eldgui.event.ClickHandler;
import com.ericlam.mc.eldgui.event.DragHandler;
import com.ericlam.mc.eldgui.lifecycle.OnDestroy;
import com.ericlam.mc.eldgui.view.JumpToView;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

@ControllerForView(CraftTableView.class)
public class CraftTableController implements UIController {

    @ClickHandler(base = @BaseHandler(patterns = {'B'}))
    public JumpToView onClickCraft(@FromPattern('A') List<ItemStack> items, UISession session, Player player) {

        if (is9Diamond(items)) {
            session.setAttribute("craft_result", Material.DIAMOND_BLOCK);
        } else if (is9Iron(items)) {
            session.setAttribute("craft_result", Material.IRON_BLOCK);
        }

        if (session.getAttribute("craft_result") != null){
            return new JumpToView("confirm", true);
        }else{
            player.sendMessage("no craft result!");
        }

        return null;
    }

    @DragHandler(base = @BaseHandler(patterns = {'A'}))
    public void onDrag(@FromPattern(value = 'A', fromDrag = true) List<ItemStack> items, LiveData<CraftTableModel> tableModelLiveData, Player player){
        player.sendMessage("size: "+items.size());
        player.sendMessage(items.stream().map(item -> item.getType().name()).collect(Collectors.joining(", ")));
        if (is9Diamond(items)) {
            tableModelLiveData.update(m -> m.setCraftResult(new ItemStack(Material.DIAMOND_BLOCK)));
        } else if (is9Iron(items)) {
            tableModelLiveData.update(m -> m.setCraftResult(new ItemStack(Material.IRON_BLOCK)));
        }
    }

    @ClickHandler(base = @BaseHandler(patterns = {'X'}))
    public void onResultGet(LiveData<CraftTableModel> liveData, @FromPattern('A') List<ItemStack> crafts, Player player, InventoryClickEvent e){
        player.sendMessage("crafts size: "+crafts.size());
        player.sendMessage(crafts.stream().map(item -> item.getType().name()).collect(Collectors.joining(", ")));
        player.sendMessage("event cancelled: "+e.isCancelled());
        if (isEmpty(crafts)) return;
        player.sendMessage("set to empty");
        liveData.update(m -> m.setResultGet(true));
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
        return itemStacks.size() == 9 && itemStacks.stream().allMatch(i -> i.getType() == Material.DIAMOND);
    }

    private boolean is9Iron(List<ItemStack> itemStacks) {
        return itemStacks.size() == 9 && itemStacks.stream().allMatch(i -> i.getType() == Material.IRON_INGOT);
    }

    private boolean isEmpty(List<ItemStack> itemStacks){
        return itemStacks.isEmpty() || itemStacks.stream().allMatch(i -> i.getType() == Material.AIR);
    }

}
