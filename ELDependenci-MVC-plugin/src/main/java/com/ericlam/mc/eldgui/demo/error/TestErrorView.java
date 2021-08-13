package com.ericlam.mc.eldgui.demo.error;

import com.ericlam.mc.eldgui.component.factory.ButtonFactory;
import com.ericlam.mc.eldgui.view.UIContext;
import com.ericlam.mc.eldgui.view.View;
import com.ericlam.mc.eldgui.view.ViewDescriptor;
import org.bukkit.Material;

@ViewDescriptor(
        name = "Test Error",
        rows = 1,
        patterns = {"ZZZZAZZZZ"}
)
public class TestErrorView implements View<Object> {


    @Override
    public void renderView(Object model, UIContext context) {
        context.pattern('A')
                .components(
                        context.factory(ButtonFactory.class)
                                .icon(Material.REDSTONE_TORCH)
                                .title("&cClick to me produce Error")
                                .lore("&eclick this will throw IllegalStateException")
                                .create()
                );
    }
}
