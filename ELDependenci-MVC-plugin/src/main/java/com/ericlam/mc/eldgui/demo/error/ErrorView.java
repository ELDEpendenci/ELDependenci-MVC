package com.ericlam.mc.eldgui.demo.error;

import com.ericlam.mc.eldgui.component.factory.ButtonFactory;
import com.ericlam.mc.eldgui.view.UIContext;
import com.ericlam.mc.eldgui.view.View;
import com.ericlam.mc.eldgui.view.ViewDescriptor;
import org.bukkit.Material;

@ViewDescriptor(
        name = "Error Encountered.",
        rows = 2,
        patterns = {
                "ZZZZAZZZZ",
                "BZZZZZZZZ"
        },
        cancelMove = {'A', 'Z'}
)
public class ErrorView implements View<Exception> {


    @Override
    public void renderView(Exception ex, UIContext context) {
        ButtonFactory button = context.factory(ButtonFactory.class);

        context
                .pattern('Z')
                .fill(button.icon(Material.BLACK_STAINED_GLASS_PANE).create())
                .and()
                .pattern('A')
                .components(
                        button.icon(Material.BARRIER)
                                .title("&cError: " + ex.getClass().getSimpleName())
                                .lore("&c".concat(ex.getMessage()))
                                .create()
                )
                .and()
                .pattern('B')
                .components(
                        button.icon(Material.GOLD_INGOT).title("&eClick Me to back to previous page").create()
                );

    }


}
