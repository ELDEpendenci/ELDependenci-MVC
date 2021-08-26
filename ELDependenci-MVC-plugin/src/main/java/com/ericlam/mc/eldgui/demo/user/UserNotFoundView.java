package com.ericlam.mc.eldgui.demo.user;

import com.ericlam.mc.eldgui.component.factory.ButtonFactory;
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

    @Override
    public void renderView(String username, UIContext context) {
        ButtonFactory button = context.factory(ButtonFactory.class);
        context.pattern('A')
                .components(
                        button.icon(Material.BARRIER)
                                .title("&cUser with username " + username + " is not found.")
                                .lore("&eClick to back to previous page")
                                .create()
                );
    }
}
