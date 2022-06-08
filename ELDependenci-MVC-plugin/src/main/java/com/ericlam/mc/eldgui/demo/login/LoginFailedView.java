package com.ericlam.mc.eldgui.demo.login;

import com.ericlam.mc.eldgui.component.factory.ButtonFactory;
import com.ericlam.mc.eldgui.view.UIContext;
import com.ericlam.mc.eldgui.view.View;
import com.ericlam.mc.eldgui.view.ViewDescriptor;
import org.bukkit.Material;

@ViewDescriptor(
        name = "登入失敗!",
        rows = 1,
        patterns = "AAAABAAAC",
        cancelMove = {'A', 'B', 'C'}
)
public class LoginFailedView implements View<Void> {

    @Override
    public void renderView(Void model, UIContext context) {
        var btn = context.factory(ButtonFactory.class);
        context.pattern('B')
                .components(
                        btn.icon(Material.BARRIER)
                                .title("&a登入失敗!")
                                .lore("&e無效的賬戶ID或密碼。")
                                .create()
                )
                .and()
                .pattern('C')
                .components(
                        btn.icon(Material.DIAMOND_PICKAXE)
                                .title("&b返回登入界面")
                                .create()
                );
    }
}
