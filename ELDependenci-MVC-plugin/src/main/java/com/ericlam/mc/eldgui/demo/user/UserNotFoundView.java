package com.ericlam.mc.eldgui.demo.user;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.view.UIContext;
import com.ericlam.mc.eldgui.view.View;
import com.ericlam.mc.eldgui.view.ViewDescriptor;
import org.bukkit.Material;

@ViewDescriptor(
        name = "User not found",
        rows = 1,
        patterns = {"ZZZZAZZZZ"},
        cancelMove = {'A', 'Z'}
)
public class UserNotFoundView implements View<String> {

    private ItemStackService itemStackService;

    @Override
    public void setItemStackService(ItemStackService itemStackService) {
        this.itemStackService = itemStackService;
    }

    @Override
    public void renderView(String username, UIContext context) {
        context.addItem('A', itemStackService
                .build(Material.BARRIER)
                .display("&cUser with username "+username+" is not found.")
                .lore("&eClick to back to previous page")
                .getItem()
        );
    }
}
