package com.ericlam.mc.eldgui;

import com.ericlam.mc.eld.services.ConfigPoolService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DemoInventoryManager {


    private ConfigPoolService configPoolService;

    @Inject
    private InventoryFactoryService factoryService;

    private InventoryUI inventoryUI;

    public void buildAppleShopInventory(){
        var ui = factoryService.generateFromTemplate("apple-shop");
        ui.addClickEvent('A', ClickCondition.name("trade"), e -> {
            var player = (Player)e.getWhoClicked();


        });
    }

    public void mergeInventory(Inventory inventory, Material m, int amount){
        var items = Arrays.stream(inventory.getContents()).collect(Collectors.groupingByConcurrent(ItemStack::getType));
        items.forEach((material, itemList) -> {
            var size = itemList.size();
            if (material == m && size > amount){
                inventory.setContents(items.merge(m, List.of(), (itemStacks, itemStacks2) -> itemList.subList(0, size - amount)).toArray(new ItemStack[0]));
            }
        });
    }
}
