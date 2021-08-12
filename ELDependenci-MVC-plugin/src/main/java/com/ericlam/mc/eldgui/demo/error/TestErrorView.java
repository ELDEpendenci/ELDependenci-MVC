package com.ericlam.mc.eldgui.demo.error;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.view.UIContext;
import com.ericlam.mc.eldgui.view.View;
import com.ericlam.mc.eldgui.view.ViewDescriptor;
import org.bukkit.Material;

@ViewDescriptor(
        name = "Test Error",
        rows = 1,
        patterns = { "ZZZZAZZZZ" }
)
public class TestErrorView implements View<Object> {

    private ItemStackService itemStackService;

    @Override
    public void setItemStackService(ItemStackService itemStackService) {
        this.itemStackService = itemStackService;
    }

    @Override
    public void renderView(Object model, UIContext context) {
        context.addItem('A', itemStackService
                .build(Material.REDSTONE_TORCH)
                .display("&cClick to me produce Error")
                .lore("&eclick this will throw IllegalStateException")
                .getItem());
    }
}
