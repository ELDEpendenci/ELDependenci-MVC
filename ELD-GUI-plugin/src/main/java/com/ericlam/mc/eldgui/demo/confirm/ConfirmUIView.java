package com.ericlam.mc.eldgui.demo.confirm;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.UIContext;
import com.ericlam.mc.eldgui.UISession;
import com.ericlam.mc.eldgui.demo.DemoInventories;
import com.ericlam.mc.eldgui.view.UseTemplate;
import com.ericlam.mc.eldgui.view.View;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Locale;

@UseTemplate(
        template = "confirm",
        groupResource = DemoInventories.class
)
public class ConfirmUIView extends View<ConfirmModel> {

    public ConfirmUIView(ItemStackService itemStackService) {
        super(itemStackService);
    }

    @Override
    public ConfirmModel renderAndCreateModel(UISession session, UIContext context, Player player) {
        Material m = session.getAttribute("craft_result");
        context.setItem('C', 0,
                itemStackService
                        .build(Material.DIAMOND_PICKAXE)
                        .display("&b即將合成: ".concat(m != null ? m.name().toLowerCase(Locale.ROOT) : "無"))
                        .lore("&e確定合成?")
                        .getItem());
        itemStackService.build(Material.PAPER).getItem().getItemMeta().getPersistentDataContainer();
        return new ConfirmModel();
    }

    @Override
    public void onModelChanged(ConfirmModel model, UIContext context, Player player) {
    }
}
