package com.ericlam.mc.eldgui.view;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.UIContext;
import com.ericlam.mc.eldgui.UISession;
import com.ericlam.mc.eldgui.model.Model;
import org.bukkit.entity.Player;

@Deprecated
public abstract class View_Legacy<T extends Model> {

    protected final ItemStackService itemStackService;

    public View_Legacy(ItemStackService itemStackService) {
        this.itemStackService = itemStackService;
    }

    public abstract T renderAndCreateModel(UISession session, UIContext context, Player player);

    public abstract void onModelChanged(T model, UIContext context, Player player);

    public void onResume(UISession session, UIContext context, Player player) {
    }

    public boolean persist() {
        return false;
    }

}
