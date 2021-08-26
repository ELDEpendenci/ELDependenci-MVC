package com.ericlam.mc.eldgui.demo;

import com.ericlam.mc.eldgui.component.factory.AnimatedButtonFactory;
import com.ericlam.mc.eldgui.view.LoadingView;
import com.ericlam.mc.eldgui.view.UIContext;
import com.ericlam.mc.eldgui.view.ViewDescriptor;
import org.bukkit.Material;

@ViewDescriptor(
        name = "Loading...",
        rows = 1,
        patterns = {"ZZZZZZZZZ"},
        cancelMove = {'Z'}
)
public final class ELDGLoadingView implements LoadingView {

    @Override
    public void renderView(Void model, UIContext context) {
        AnimatedButtonFactory animatedButton = context.factory(AnimatedButtonFactory.class);
        context.pattern('Z')
                .fill(
                        animatedButton.interval(1)
                                .icons(
                                        Material.GREEN_STAINED_GLASS_PANE,
                                        Material.RED_STAINED_GLASS_PANE,
                                        Material.BLUE_STAINED_GLASS_PANE,
                                        Material.BLACK_STAINED_GLASS_PANE,
                                        Material.WHITE_STAINED_GLASS_PANE
                                ).create()
                );
    }

}