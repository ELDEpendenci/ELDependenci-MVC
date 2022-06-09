package com.ericlam.mc.eldgui.demo.login;

import com.ericlam.mc.eldgui.component.factory.ButtonFactory;
import com.ericlam.mc.eldgui.view.UIContext;
import com.ericlam.mc.eldgui.view.View;
import com.ericlam.mc.eldgui.view.ViewDescriptor;
import org.bukkit.Material;

@ViewDescriptor(
        name = "管理員界面",
        rows = 1,
        patterns = "AAAABAAAC",
        cancelMove = {'A', 'B', 'C'}
)
public class AdminContentView implements View<Void> {
    @Override
    public void renderView(Void model, UIContext context) {
        var btn = context.factory(ButtonFactory.class);

        context.pattern('B')
                .components(
                        btn.icon(Material.REDSTONE_BLOCK)
                                .title("&b這裡是管理員界面，只有管理員才可以訪問這裏。")
                                .create()
                )
                .and()
                .pattern('C')
                .components(
                        btn.icon(Material.EMERALD)
                                .title("&e返回主界面。")
                                .create()
                );
    }
}
