package com.ericlam.mc.eldgui.demo.crafttable;

import com.ericlam.mc.eldgui.model.Model;
import org.bukkit.inventory.ItemStack;

public class CraftTableModel extends Model {

    private ItemStack craftResult;
    private boolean resultGet;

    public ItemStack getCraftResult() {
        return craftResult;
    }

    public void setCraftResult(ItemStack craftResult) {
        this.craftResult = craftResult;
    }

    public boolean isResultGet() {
        return resultGet;
    }

    public void setResultGet(boolean resultGet) {
        this.resultGet = resultGet;
    }
}
