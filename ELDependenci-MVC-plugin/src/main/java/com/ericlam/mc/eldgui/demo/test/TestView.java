package com.ericlam.mc.eldgui.demo.test;

import com.ericlam.mc.eldgui.component.factory.RGBSelectorFactory;
import com.ericlam.mc.eldgui.view.UIContext;
import com.ericlam.mc.eldgui.view.View;
import com.ericlam.mc.eldgui.view.ViewDescriptor;
import org.bukkit.Color;

@ViewDescriptor(
        name = "Test GUI",
        rows = 1,
        patterns = {"ZZZZZZZZZ"},
        cancelMove = {'Z'}
)
public class TestView implements View<Void> {

    @Override
    public void renderView(Void model, UIContext context) {
        RGBSelectorFactory rgbSelector = context.factory(RGBSelectorFactory.class);
        context.pattern('Z')
                .components(
                        rgbSelector
                                .bindInput("idk", Color.WHITE)
                                .label("&aColor Select: (shift to move color, click to +/-, middle to input)")
                                .create()
                );
    }

}
