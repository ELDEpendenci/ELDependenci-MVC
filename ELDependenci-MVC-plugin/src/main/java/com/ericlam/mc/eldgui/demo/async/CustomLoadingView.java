package com.ericlam.mc.eldgui.demo.async;

import com.ericlam.mc.eldgui.component.factory.AnimatedButtonFactory;
import com.ericlam.mc.eldgui.view.LoadingView;
import com.ericlam.mc.eldgui.view.UIContext;
import com.ericlam.mc.eldgui.view.ViewDescriptor;
import org.bukkit.Material;

@ViewDescriptor(
        name = "async custom loading",
        rows = 1,
        patterns = "ZZZZZZZZZ",
        cancelMove = 'Z'
)
public final class CustomLoadingView implements LoadingView {


    @Override
    public void renderView(Void model, UIContext context) {
        AnimatedButtonFactory animatedButton = context.factory(AnimatedButtonFactory.class);
        context.pattern('Z').fill(animatedButton.icons(Material.APPLE, Material.BEETROOT_SOUP, Material.CARROT).interval(1).numbers(1, 2, 3, 4, 5).create());
    }
}
