package com.ericlam.mc.eldgui.demo.user;

import com.ericlam.mc.eldgui.component.factory.ButtonFactory;
import com.ericlam.mc.eldgui.component.factory.NumInputFactory;
import com.ericlam.mc.eldgui.component.factory.SelectionFactory;
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
                "ZZAZBZAZZ"
        },
        cancelMove = {'Z', 'A', 'B'}
)
public class UserUpdateView implements View<User> {

    @Override
    public void renderView(@Nullable User model, UIContext context) {

        TextInputFactory textField = context.factory(TextInputFactory.class);
        NumInputFactory numberField = context.factory(NumInputFactory.class);
        ButtonFactory button = context.factory(ButtonFactory.class);
        SelectionFactory selection = context.factory(SelectionFactory.class);

        var usernameField = textField
                .icon(Material.BIRCH_SIGN)
                .label("Enter UserName")
                .bindInput("username", model == null ? "" : model.username);

        if (model != null) usernameField = usernameField.disabled(); // if edit mode, disable the input

        // model == null is create mode, else edit mode
        context.pattern('A')
                .components(
                        usernameField.create(),
                        /*
                        textField
                                .icon(Material.ACACIA_SIGN)
                                .label("Enter First Name")
                                .bindInput("firstName", model == null ? "" : model.firstName)
                                .create(),

                         */
                        selection
                                .icon(Material.ACACIA_SIGN)
                                .selectable(String.class, builder -> {

                                    builder.insert("Lam").icon(Material.GOLD_INGOT).number(1).submit();
                                    builder.insert("Chan").icon(Material.IRON_INGOT).number(2).submit();
                                    builder.insert("Wong").icon(Material.EMERALD).number(3).submit();
                                    builder.insert("Siu").icon(Material.REDSTONE).number(4).submit();
                                    builder.insert("Lai").icon(Material.DIAMOND).number(5).submit();

                                })
                                .bindInput("firstName", model == null ? "Lam" : model.firstName)
                                .then()
                                .create(),
                        textField
                                .icon(Material.CRIMSON_SIGN)
                                .label("Enter Last Name")
                                .bindInput("lastName", model == null ? "" : model.lastName)
                                .create(),
                        numberField
                                .icon(Material.REDSTONE_TORCH)
                                .label("Input Age (Left [+] / Right [-] / Middle [Input Number])")
                                .useNumberType(Integer.class)
                                .bindInput("age", model == null ? 0 : model.age)
                                .min(0)
                                .max(64)
                                .step(1)
                                .then()
                                .create(),
                        textField
                                .icon(Material.MAP)
                                .label("Enter Address Line1: ")
                                .bindInput("address.line1", model == null ? "" : model.address.line1)
                                .create(),
                        textField
                                .icon(Material.MAP)
                                .label("Enter Address Line2: ")
                                .bindInput("address.line2", model == null ? "" : model.address.line2)
                                .number(2)
                                .create()
                )
                .and()
                .pattern('B')
                .components(
                        button.icon(Material.DIAMOND_BLOCK).title("Submit").create()
                );
    }
}
