package com.ericlam.mc.eldgui.demo.user;

import com.ericlam.mc.eldgui.component.factory.ButtonFactory;
import com.ericlam.mc.eldgui.component.factory.NumInputFactory;
import com.ericlam.mc.eldgui.component.factory.TextInputFactory;
import com.ericlam.mc.eldgui.view.UIContext;
import com.ericlam.mc.eldgui.view.View;
import com.ericlam.mc.eldgui.view.ViewDescriptor;
import org.bukkit.Material;

import javax.annotation.Nullable;


@ViewDescriptor(
        name = "update/create user",
        rows = 2,
        patterns = {
                "ZAZAZAZAZ",
                "ZZZZBZZZZ"
        }
)
public class UserUpdateView implements View<User> {

    @Override
    public void renderView(@Nullable User model, UIContext context) {

        TextInputFactory textField = context.factory(TextInputFactory.class);
        NumInputFactory numberField = context.factory(NumInputFactory.class);
        ButtonFactory button = context.factory(ButtonFactory.class);

        context.pattern('A')
                .components(
                        textField
                                .icon(Material.BIRCH_SIGN)
                                .label("Enter Your UserName")
                                .bindInput("username", model == null ? "" : model.username)
                                .create(),
                        textField
                                .icon(Material.ACACIA_SIGN)
                                .label("Enter Your First Name")
                                .bindInput("firstname", model == null ? "" : model.firstName)
                                .create(),
                        textField
                                .icon(Material.CRIMSON_SIGN)
                                .label("Enter Your Last Name")
                                .bindInput("lastname", model == null ? "" : model.lastName)
                                .create(),
                        numberField
                                .icon(Material.REDSTONE_TORCH)
                                .label("Your Age (Left Click + / Right Click -)")
                                .bindInput("age", model == null ? 0 : model.age)
                                .create()
                )
                .and()
                .pattern('B')
                .components(
                        button.icon(Material.DIAMOND_BLOCK).title("Submit").create()
                );
    }
}
