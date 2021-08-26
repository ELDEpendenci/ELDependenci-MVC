package com.ericlam.mc.eldgui.demo.user;

import com.ericlam.mc.eldgui.component.factory.ButtonFactory;
import com.ericlam.mc.eldgui.demo.DemoInventories;
import com.ericlam.mc.eldgui.view.UIContext;
import com.ericlam.mc.eldgui.view.UseTemplate;
import com.ericlam.mc.eldgui.view.View;
import org.bukkit.Material;

import java.util.List;

@UseTemplate(
        template = "user-list",
        groupResource = DemoInventories.class
)
public class UserListView implements View<List<String>> {

    @Override
    public void renderView(List<String> users, UIContext context) {
        var button = context.factory(ButtonFactory.class);
        var builder = context.pattern('L');
        for (int i = 0; i < users.size(); i++) {
            String username = users.get(i);

            builder.components(
                    button
                            .icon(Material.PLAYER_HEAD)
                            .number(i + 1)
                            .title("&aUsername: &e" + username)
                            .lore("&eClick to check the user info")
                            .bind("username", username)
                            .create()
            );
        }

        // for not found test
        builder.components(
                button
                        .title("&aUsername: &eunknown")
                        .number(users.size() + 1)
                        .lore("&eClick to check the user info")
                        .bind("username", "unknown")
                        .create()
        );

    }
}
