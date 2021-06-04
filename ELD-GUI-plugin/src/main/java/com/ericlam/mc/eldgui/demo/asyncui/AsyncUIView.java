package com.ericlam.mc.eldgui.demo.asyncui;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.UIContext;
import com.ericlam.mc.eldgui.UISession;
import com.ericlam.mc.eldgui.view.View;
import com.ericlam.mc.eldgui.view.ViewDescriptor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@ViewDescriptor(
        rows = 1,
        name = "&aAsyncUI Demo",
        patterns = {"ZZZZAZZZZ"},
        cancelMove = {'A', 'Z'}
)
public class AsyncUIView extends View<AsyncUIModel> {

    public AsyncUIView(ItemStackService itemStackService) {
        super(itemStackService);
    }

    @Override
    public AsyncUIModel renderAndCreateModel(UISession session, UIContext context, Player player) {
        AsyncUIModel model = new AsyncUIModel();
        model.setTextFromAsync("加載中...");
        context.setItem('A', 0, itemStackService.build(Material.RED_WOOL).display("§c".concat(model.getTextFromAsync())).getItem());
        return model;
    }

    @Override
    public void onModelChanged(AsyncUIModel model, UIContext context, Player player) {
        context.setItem('A', 0, itemStackService.build(Material.GREEN_WOOL).display("§a".concat(model.getTextFromAsync())).getItem());
    }
}
