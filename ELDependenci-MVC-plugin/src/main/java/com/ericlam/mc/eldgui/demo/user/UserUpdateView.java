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
        },
        cancelMove = {'Z', 'A', 'B'}
)
public class UserUpdateView implements View<User> {

    @Override
    public void renderView(@Nullable User model, UIContext context) {

        TextInputFactory textField = context.factory(TextInputFactory.class);
        NumInputFactory numberField = context.factory(NumInputFactory.class);
        ButtonFactory button = context.factory(ButtonFactory.class);

        var usernameField = textField
                .icon(Material.BIRCH_SIGN)
                .label("Enter UserName")
                .bindInput("username", model == null ? "" : model.username);

        if (model != null) usernameField = usernameField.disabled(); // if edit mode, disable the input

        // model == null is create mode, else edit mode
        context.pattern('A')
                .components(
                        usernameField.create(),
                        textField
                                .icon(Material.ACACIA_SIGN)
                                .label("Enter First Name")
                                .bindInput("firstName", model == null ? "" : model.firstName)
                                .create(),
                        textField
                                .icon(Material.CRIMSON_SIGN)
                                .label("Enter Last Name")
                                .bindInput("lastName", model == null ? "" : model.lastName)
                                .create(),
                        numberField
                                .icon(Material.REDSTONE_TORCH)
                                .label("Input Age (Left Click + / Right Click -)")
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
