package com.ericlam.mc.eldgui.demo.login;

import com.ericlam.mc.eldgui.component.factory.ButtonFactory;
import com.ericlam.mc.eldgui.component.factory.PasswordInputFactory;
import com.ericlam.mc.eldgui.component.factory.TextInputFactory;
import com.ericlam.mc.eldgui.view.UIContext;
import com.ericlam.mc.eldgui.view.View;
import com.ericlam.mc.eldgui.view.ViewDescriptor;
import org.bukkit.Material;

@ViewDescriptor(
        name = "登入界面",
        rows = 3,
        patterns = {
                "AAAABAAAA",
                "AACAAACAA",
                "AAAAAAAAE"
        },
        cancelMove = {'B', 'C', 'A', 'E'}
)
public class LoginView implements View<Void> {

    @Override
    public void renderView(Void model, UIContext context) {

        var btn = context.factory(ButtonFactory.class);
        var textInput = context.factory(TextInputFactory.class);
        var passwordInput = context.factory(PasswordInputFactory.class);

        context.pattern('B')
                .components(
                        btn.icon(Material.DIAMOND_BLOCK)
                                .title("&e請先登入再執行之後的操作。")
                                .create()
                )
                .and()
                .pattern('C')
                .components(
                        textInput
                                .icon(Material.PAPER)
                                .label("&e輸入你的賬戶 ID")
                                .bindInput("username", null)
                                .messageInput("請輸入你的賬戶 ID")
                                .create(),
                        passwordInput
                                .icon(Material.PAPER)
                                .label("&e輸入你的密碼")
                                .bindInput("password")
                                .create()
                )
                .and()
                .pattern('E')
                .components(
                        btn.icon(Material.DIAMOND)
                                .title("&e登入")
                                .create()
                );
    }


}
