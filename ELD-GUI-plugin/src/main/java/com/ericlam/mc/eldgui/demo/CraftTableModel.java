package com.ericlam.mc.eldgui.demo;

import com.ericlam.mc.eldgui.model.Model;
import org.bukkit.inventory.ItemStack;

public class CraftTableModel extends Model {

    private ItemStack craftResult;

    public ItemStack getCraftResult() {
        return craftResult;
    }

    public void setCraftResult(ItemStack craftResult) {
        this.craftResult = craftResult;
    }
}
