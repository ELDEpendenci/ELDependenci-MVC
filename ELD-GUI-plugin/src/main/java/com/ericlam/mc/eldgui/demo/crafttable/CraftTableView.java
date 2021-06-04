package com.ericlam.mc.eldgui.demo.crafttable;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.UIContext;
import com.ericlam.mc.eldgui.UISession;
import com.ericlam.mc.eldgui.demo.DemoInventories;
import com.ericlam.mc.eldgui.view.UseTemplate;
import com.ericlam.mc.eldgui.view.View;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@UseTemplate(
        template = "crafttable",
        groupResource = DemoInventories.class
)
public class CraftTableView extends View<CraftTableModel> {

    public CraftTableView(ItemStackService itemStackService) {
        super(itemStackService);
    }

    @Override
    public CraftTableModel renderAndCreateModel(UISession session, UIContext context, Player player) {
        return new CraftTableModel();
    }

    @Override
    public void onResume(UISession session, UIContext context, Player player) {
        Boolean confirm = session.getAttribute("confirm");
        if (confirm != null) {
            if (confirm) {
                player.sendMessage("you clicked confirm!");
                Material m = session.getAttribute("craft_result");
                if (m == null) {
                    player.sendMessage("Nothing Crafted.");
                    return;
                }
                ItemStack result = new ItemStack(m);
                context.setItem('X', 0, result);
                context.fillItem('A', null);
                player.sendMessage("you crafted ".concat(m.name()));
            } else {
                player.sendMessage("you clicked cancel, nothing changed.");
            }
        }
        // delete old attributes after resume
        session.removeAttribute("confirm");
        session.removeAttribute("craft_result");
    }

    @Override
    public void onModelChanged(CraftTableModel model, UIContext context, Player player) {
    }
}
