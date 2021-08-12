package com.ericlam.mc.eldgui.demo.user;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.view.UIContext;
import com.ericlam.mc.eldgui.demo.DemoInventories;
import com.ericlam.mc.eldgui.view.UseTemplate;
import com.ericlam.mc.eldgui.view.View;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@UseTemplate(
        template = "user-list",
        groupResource = DemoInventories.class
)
public class UserListView implements View<List<String>> {

    private ItemStackService itemStackService;

    @Override
    public void setItemStackService(ItemStackService itemStackService) {
        this.itemStackService = itemStackService;
    }

    @Override
    public void renderView(List<String> users, UIContext context) {
        for (int i = 0; i < users.size(); i++) {
            String username = users.get(i);
            ItemStack userButton = itemStackService
                    .build(Material.APPLE)
                    .amount(i + 1)
                    .display("&aUsername: &e" + username)
                    .lore("&eClick to check the user info")
                    .getItem();
            context.setAttribute(String.class, userButton, "username", username);
            context.addItem('L', userButton);
        }

        ItemStack nonExistUserBtn = itemStackService
                .build(Material.APPLE)
                .amount(users.size()+1)
                .display("&aUsername: &eunknown")
                .lore("&eClick to check the user info")
                .getItem();
        context.setAttribute(String.class, nonExistUserBtn, "username", "unknown");
        context.addItem('L', nonExistUserBtn);

    }
}
