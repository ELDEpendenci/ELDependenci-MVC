package com.ericlam.mc.eldgui.demo.login;

import com.ericlam.mc.eldgui.component.factory.ButtonFactory;
import com.ericlam.mc.eldgui.view.UIContext;
import com.ericlam.mc.eldgui.view.View;
import com.ericlam.mc.eldgui.view.ViewDescriptor;
import org.bukkit.Material;

@ViewDescriptor(
        name = "&6主界面",
        rows = 1,
        patterns = "DEAABAAAC",
        cancelMove = {'A', 'B', 'C', 'D', 'E'}
)
public class MainView implements View<Void> {

    @Override
    public void renderView(Void model, UIContext context) {
        var btn = context.factory(ButtonFactory.class);

        context.pattern('B')
                .components(
                        btn.icon(Material.DIAMOND_BLOCK)
                                .title("&b當你看到這個界面，代表你目前已經登入。")
                                .create()
                )
                .and()
                .pattern('C')
                .components(
                        btn.icon(Material.DIAMOND_PICKAXE)
                                .title("&b登出")
                                .create()
                )
                .and()
                .pattern('D')
                .components(
                        btn.icon(Material.REDSTONE_BLOCK)
                                .title("&c進入管理員界面。")
                                .create()
                )
                .and()
                .pattern('E')
                .components(
                        btn.icon(Material.PAPER)
                                .title("&e查看你的個人資訊")
                                .create()
                );

    }
}
