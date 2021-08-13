package com.ericlam.mc.eldgui.demo.user;

import com.ericlam.mc.eldgui.component.factory.ButtonFactory;
import com.ericlam.mc.eldgui.demo.DemoInventories;
import com.ericlam.mc.eldgui.view.UIContext;
import com.ericlam.mc.eldgui.view.UseTemplate;
import com.ericlam.mc.eldgui.view.View;
import org.bukkit.Material;

@UseTemplate(
        template = "user",
        groupResource = DemoInventories.class
)
public class UserView implements View<User> {

    @Override
    public void renderView(User model, UIContext context) {
        ButtonFactory button = context.factory(ButtonFactory.class);

        context.pattern('A')
                .components(
                        button
                                .icon(Material.DIAMOND)
                                .title("&cUser Info")
                                .lore(
                                        "&aUsername: &f" + model.username,
                                        "&aFirst Name: &f" + model.firstName,
                                        "&aLast Name: &f" + model.lastName,
                                        "&aAge: &f" + model.age
                                ).create()
                )
                .and()
                .pattern('C').bindAll("to-delete", model.username)
                .and()
                .pattern('D').bindAll("to-edit", model.username);
    }
}
