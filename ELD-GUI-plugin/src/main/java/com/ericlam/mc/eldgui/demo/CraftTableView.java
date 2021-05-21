package com.ericlam.mc.eldgui.demo;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.InventoryTemplate;
import com.ericlam.mc.eldgui.UIContext;
import com.ericlam.mc.eldgui.UISession;
import com.ericlam.mc.eldgui.view.UseTemplate;
import com.ericlam.mc.eldgui.view.View;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

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
        context.setItem('W', 0, itemStackService.build(Material.PAPER)
                .display("數值")
                .lore(List.of(
                        "&6[傳遞數值]: "+session.getAttribute("pass"),
                        "&b上一個背包: "+session.getAttribute("previous_page")
                )).getItem());

        return new CraftTableModel();
    }

    @Override
    public void onModelChanged(CraftTableModel model, UIContext context, Player player) {
        var item = model.getCraftResult();
        if (item != null){
            context.setItem('X', 0, item);
            context.fillItem('A', null);
        }
        model.setCraftResult(null);
    }
}
