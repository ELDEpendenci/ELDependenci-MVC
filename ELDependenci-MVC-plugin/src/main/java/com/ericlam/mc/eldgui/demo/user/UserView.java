package com.ericlam.mc.eldgui.demo.user;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.view.UIContext;
import com.ericlam.mc.eldgui.demo.DemoInventories;
import com.ericlam.mc.eldgui.view.UseTemplate;
import com.ericlam.mc.eldgui.view.View;
import org.bukkit.Material;

import java.util.List;

@UseTemplate(
        template = "user",
        groupResource = DemoInventories.class
)
public class UserView implements View<User> {

    private ItemStackService itemStackService;

    @Override
    public void setItemStackService(ItemStackService itemStackService) {
        this.itemStackService = itemStackService;
    }

    @Override
    public void renderView(User model, UIContext context) {
        context.addItem('A', itemStackService
                .build(Material.DIAMOND)
                .display("&cUser Info")
                .lore(List.of(
                        "&aUsername: &f"+model.username,
                        "&aFirst Name: &f"+model.firstName,
                        "&aLast Name: &f"+model.lastName,
                        "&aAge: &f"+model.age
                ))
                .getItem()
        );

        context.setAttribute(String.class, 'C', "to-delete", model.username);
    }
}
