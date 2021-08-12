package com.ericlam.mc.eldgui.demo.error;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.view.UIContext;
import com.ericlam.mc.eldgui.view.View;
import com.ericlam.mc.eldgui.view.ViewDescriptor;
import org.bukkit.Material;

@ViewDescriptor(
        name = "Error Encountered",
        rows = 1,
        patterns = {"ZZZZAZZZZ"},
        cancelMove = {'A', 'Z'}
)
public class StaticErrorView implements View<Exception> {

    private ItemStackService itemStackService;

    @Override
    public void setItemStackService(ItemStackService itemStackService) {
        this.itemStackService = itemStackService;
    }

    @Override
    public void renderView(Exception ex, UIContext context) {
        context.fillItem('Z', itemStackService.build(Material.BLACK_STAINED_GLASS_PANE).getItem());
        context.addItem('A', itemStackService
                .build(Material.BARRIER)
                .display("&cError: "+ex.getClass().getSimpleName())
                .lore("&c".concat(ex.getMessage()))
                .getItem()
        );
    }
}
