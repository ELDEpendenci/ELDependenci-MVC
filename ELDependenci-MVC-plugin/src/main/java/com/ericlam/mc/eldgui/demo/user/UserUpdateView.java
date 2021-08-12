package com.ericlam.mc.eldgui.demo.user;

import com.ericlam.mc.eldgui.component.factory.ButtonFactory;
import com.ericlam.mc.eldgui.component.factory.NumInputFactory;
import com.ericlam.mc.eldgui.component.factory.TextInputFactory;
import com.ericlam.mc.eldgui.view.UIContext;
import com.ericlam.mc.eldgui.view.View;
import com.ericlam.mc.eldgui.view.ViewContext;
import com.ericlam.mc.eldgui.view.ViewDescriptor;
import org.bukkit.Material;


@ViewDescriptor(
        name = "update/create user",
        rows = 1,
        patterns = { "ZAZAZAZAZ" }
)
public class UserUpdateView implements View<User> {

    @Override
    public void renderView(User model, UIContext context) {
    }


    @Override
    public void renderView(User model, ViewContext context) {

        TextInputFactory textField = context.factory(TextInputFactory.class);
        NumInputFactory numberField = context.factory(NumInputFactory.class);
        ButtonFactory button = context.factory(ButtonFactory.class);
        context.pattern('A')
                .components(
                        textField
                                .icon(Material.BIRCH_SIGN)
                                .title("Enter Your UserName")
                                .bindInput("username", "")
                                .create(),
                        textField
                                .icon(Material.ACACIA_SIGN)
                                .title("Enter Your First Name")
                                .bindInput("firstname", "")
                                .create(),
                        textField
                                .icon(Material.CRIMSON_SIGN)
                                .title("Enter Your Last Name")
                                .bindInput("lastname", "")
                                .create(),
                        numberField
                                .icon(Material.REDSTONE_TORCH)
                                .title("Your Age is now: 0")
                                .lore("Left Click to increase", "Right Click to decrease")
                                .bindInput("age", 0)
                                .create()
                )
                .and()
                .pattern('B')
                .components(
                        button.icon(Material.DIAMOND_BLOCK).title("Submit").create()
                );
    }
}
